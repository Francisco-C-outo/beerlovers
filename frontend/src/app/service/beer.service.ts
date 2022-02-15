import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Beer } from '../model/beer';
import { BeerResponse } from '../model/beerresponse';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BeerService {

  private beerApiUrl : string;
  private favoritesAPiUrl : string;

  constructor(private http: HttpClient) { 
    this.beerApiUrl = 'http://localhost:8080/v1/beers/'
    this.favoritesAPiUrl = 'http://localhost:8080/v1/beers/favorites'
  }

  public searchAllBeers(size : number, currPage : number) : Observable<BeerResponse> {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });

    let httpParams = new HttpParams().set("page", currPage).set("size", size);

    let options = { headers: headers, params: httpParams };

    return this.http.get<BeerResponse>(this.beerApiUrl, options);
  } 

  public searchBeersByKeyword(size : number, currPage : number, keyword : string) : Observable<BeerResponse> {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });

    let httpParams = new HttpParams().set("page", currPage).set("size", size).set("keyword", keyword);

    let options = { headers: headers, params: httpParams };

    return this.http.get<BeerResponse>(this.beerApiUrl, options);
  } 

  public addBeerToFavorites(beer : Beer) : Observable<Beer> {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });


    return this.http.post<Beer>(this.favoritesAPiUrl, beer, {headers});
  } 

  public getFavoriteBeers() : Observable<Beer[]> {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });

    let options = { headers: headers };
    return this.http.get<Beer[]>(this.favoritesAPiUrl, options);
  }
}
