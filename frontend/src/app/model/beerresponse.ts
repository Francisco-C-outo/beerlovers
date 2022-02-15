import { Beer } from '../model/beer';

export class BeerResponse {
    beers: Beer[];
    totalResults: number;

    constructor(beers: Beer[], totalResults: number) {
        this.beers = beers;
        this.totalResults = totalResults;
      }
}
