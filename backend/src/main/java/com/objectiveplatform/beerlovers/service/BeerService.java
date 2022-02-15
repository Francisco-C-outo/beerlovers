package com.objectiveplatform.beerlovers.service;

import com.objectiveplatform.beerlovers.datamodel.entity.User;
import com.objectiveplatform.beerlovers.datamodel.repository.UserRepository;
import com.objectiveplatform.beerlovers.dto.Beer;
import com.objectiveplatform.beerlovers.dto.BeerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.objectiveplatform.beerlovers.util.GenericUtil.isStringAMatch;
import static java.lang.String.format;

/**
 * Source of the core functionality in our application. This services handles all the business logic required for each
 * of our exposed operations on the API layer. Extra functionality not directly related to Beers was extracted to helper
 * services like @UserService and @PunkApiService
 */
@Service
@RequiredArgsConstructor
public class BeerService {

    //value cannot be retrieved directly from the API without a bunch of extra calls so for simplicity purposes
    //I analyzed the API and concluded this is the number of beers available
    private static final int TOTAL_ITEMS = 325;
    private final UserRepository userRepository;
    private final PunkApiService punkApiService;
    private final UserService userService;

    private static List<Beer> filterBeersByKeyword(final List<Beer> beers, final String keyword) {
        return beers.stream()
                .filter(beer -> (isStringAMatch(beer.getName(), keyword) || isStringAMatch(beer.getDescription(), keyword)))
                .collect(Collectors.toList());
    }

    public BeerResponse getBeersPaginated(final int page, final int size) {
        final List<Beer> result = punkApiService.getBeersFromPunkApi(page, size);
        return new BeerResponse(result, TOTAL_ITEMS);
    }

    /**
     * This method could use some caching it would help specifically on more common requests
     */
    public BeerResponse getBeersFiltered(final int page, final int size, final String keyword) {
        List<Beer> allBeersFromPunkApi = punkApiService.getAllBeersFromPunkApi();
        final List<Beer> filteredResults = filterBeersByKeyword(allBeersFromPunkApi, keyword);
        final List<Beer> resultsForPage = filteredResults.subList(((page - 1) * size), Math.min((page * size), filteredResults.size()));
        return new BeerResponse(resultsForPage, filteredResults.size());
    }

    public Beer createFavoriteEntryForUser(final Beer beer) {
        final String userName = retrieveUserInformation();
        final Optional<User> user = userRepository.findByUsernameEquals(userName);
        if (user.isPresent()) {
            final User actualUser = user.get();
            actualUser.getFavoriteBeers().add(beer.getId());
            userRepository.save(actualUser);
            return beer;
        }
        throw new EntityNotFoundException(format("User with username %s was not found!", userName));
    }

    public void deleteFavoriteEntryForUser(final Beer beer) {
        final String userName = retrieveUserInformation();
        final Optional<User> user = userRepository.findByUsernameEquals(userName);
        if (user.isPresent()) {
            final User actualUser = user.get();
            final boolean beerWasRemoved = actualUser.getFavoriteBeers().remove(beer.getId());
            if (beerWasRemoved) {
                userRepository.save(actualUser);
            } else {
                throw new IllegalArgumentException(format("The supplied beerId to be removed (%s) was invalid!", beer.getId()));
            }
        }
        throw new EntityNotFoundException(format("User with username %s was not found!", userName));
    }

    public List<Beer> getFavoriteBeersForUser() {
        final String userName = retrieveUserInformation();
        final Optional<User> user = userRepository.findByUsernameEquals(userName);
        if (user.isPresent()) {
            final Set<Integer> externalIds = user.get().getFavoriteBeers();

            //The actual information of the beers will come from the external API
            return punkApiService.getBeersFromPunkApiById(externalIds);
        }
        throw new EntityNotFoundException(format("User with username %s was not found!", userName));
    }

    private String retrieveUserInformation() {
        return userService.retrieveAuthenticatedUser();
    }
}
