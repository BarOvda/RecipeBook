import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { UserRedistrationService } from './user-redistration.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { HeaderComponent } from './header/header.component';
import { MainLogoComponent } from './main-logo/main-logo.component';
import { LogInComponent } from './log-in/log-in.component';
import { DetailsComponent } from './details/details.component';
import { AddRecipeComponent } from './add-recipe/add-recipe.component';
import { RecipesComponent } from './recipes/recipes.component';
import { RecipeListComponent } from './recipes/recipe-list/recipe-list.component';
import { RecipeItemComponent } from './recipes/recipe-list/recipe-item/recipe-item.component';
import { RecipeDetailComponent } from './recipes/recipe-detail/recipe-detail.component';
import { SearchUsersComponent } from './search-users/search-users.component';
import { UsersDetailsComponent } from './search-users/users-details/users-details.component';



@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    HeaderComponent,
    MainLogoComponent,
    LogInComponent,
    DetailsComponent,
    AddRecipeComponent,
    RecipesComponent,
    RecipeListComponent,
    RecipeItemComponent,
    RecipeDetailComponent,
    SearchUsersComponent,
    UsersDetailsComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [UserRedistrationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
