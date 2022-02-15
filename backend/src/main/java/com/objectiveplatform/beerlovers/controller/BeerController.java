package com.objectiveplatform.beerlovers.controller;

import com.objectiveplatform.beerlovers.dto.Beer;
import com.objectiveplatform.beerlovers.dto.BeerResponse;
import com.objectiveplatform.beerlovers.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/v1/beers")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    @GetMapping(value = "/", params = {"page", "size"})
    public BeerResponse getBeers(@RequestParam("page") @Min(1) @NotBlank Integer page, @RequestParam("size") @Min(1) @NotBlank Integer size) {
        return beerService.getBeersPaginated(page, size);
    }

    @GetMapping(value = "/", params = {"page", "size", "keyword"})
    public BeerResponse getBeersFiltered(@RequestParam("page") @Min(1) @NotBlank Integer page, @RequestParam("size")  @Min(1) @NotBlank Integer size,
                                         @RequestParam("keyword") @NotBlank String keyword) {
        return beerService.getBeersFiltered(page, size, keyword);
    }

    @PostMapping("/favorites")
    public Beer addFavoriteBeerForUser(@RequestBody @Valid Beer beer) {
        return beerService.createFavoriteEntryForUser(beer);
    }

    @DeleteMapping("/favorites")
    public void deleteFavoriteBeerForUser(@RequestBody @Valid Beer beer) {
        beerService.deleteFavoriteEntryForUser(beer);
    }

    @GetMapping("/favorites")
    public List<Beer> getFavoriteBeersForUser() {
        return beerService.getFavoriteBeersForUser();
    }
}
