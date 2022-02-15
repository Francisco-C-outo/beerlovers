import { Component, OnInit } from '@angular/core';
import { Beer } from '../../model/beer';
import { BeerService } from '../../service/beer.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-beer-list',
  templateUrl: './beer-list.component.html',
  styleUrls: ['./beer-list.component.css']
})
export class BeerListComponent implements OnInit {
  beers: Beer[] = [];

  keyword = "";
  currPage = 1;
  itemsPerPage = 10;
  totalItems = 0;

  constructor(private beerService: BeerService, private toastr: ToastrService) {
  }

  ngOnInit() {
    this.retrieveBeers();
  }

  searchBeerCatalogue() : void {
    this.currPage = 1;
    this.retrieveBeers();
  }

  retrieveBeers(): void {
    if(!this.keyword || this.keyword.length === 0) {
      this.beerService.searchAllBeers(this.itemsPerPage, this.currPage).subscribe(data => {
        this.beers = data.beers;
        this.totalItems = data.totalResults;
      });
    }
    else {
      this.beerService.searchBeersByKeyword(this.itemsPerPage, this.currPage, this.keyword).subscribe(data => {
        this.beers = data.beers;
        this.keyword = '';
        this.totalItems = 2;
        this.itemsPerPage = 2;
      });
    }
  }

  addToFavorites(beer : Beer) {
    this.beerService.addBeerToFavorites(beer).subscribe(data => {
        if(data) {
          this.showSuccess("Beer added to favorites!", "Success!");
        }
        else {
          this.showError("Failed to add beer to favorites, please try again.", "Oops!");
        }
    });
  }

  handlePageChange(event: any): void {
    this.currPage = event;
    this.retrieveBeers();
  }

  showSuccess(message : string, title : string){
    this.toastr.success(message, title)
  }

  showError(message : string, title : string){
    this.toastr.error(message, title)
}
}
