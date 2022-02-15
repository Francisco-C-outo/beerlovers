import { Component, OnInit } from '@angular/core';
import { Beer } from '../../model/beer';
import { BeerService } from '../../service/beer.service';

@Component({
  selector: 'app-favorite-list',
  templateUrl: './favorite-list.component.html',
  styleUrls: ['./favorite-list.component.css']
})
export class FavoriteListComponent implements OnInit {
  favoriteBeers: Beer[] = [];

  constructor(private beerService: BeerService) {
  }

  ngOnInit() {
    this.beerService.getFavoriteBeers().subscribe(data => {
      this.favoriteBeers = data;
    });
  }
}
