import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BeerListComponent } from './component/beer-list/beer-list.component';
import { FavoriteListComponent } from './component/favorite-list/favorite-list.component';
import { LoginComponent } from './component/login/login.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'beers', component: BeerListComponent },
  { path: 'favorites', component: FavoriteListComponent },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
