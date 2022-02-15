import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { BeerListComponent } from './component/beer-list/beer-list.component';
import { BeerService } from './service/beer.service';
import { LoginComponent } from './component/login/login.component';
import { FavoriteListComponent } from './component/favorite-list/favorite-list.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    BeerListComponent,
    FavoriteListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
	  ToastrModule.forRoot()
  ],
  providers: [BeerService],
  bootstrap: [AppComponent]
})
export class AppModule { 
  constructor() {
  }
 }
