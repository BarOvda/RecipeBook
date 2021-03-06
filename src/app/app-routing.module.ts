import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { RegistrationComponent } from './registration/registration.component';
import { LogInComponent } from './log-in/log-in.component';
import { DetailsComponent } from './details/details.component';
import { AddRecipeComponent } from './add-recipe/add-recipe.component';
import { RecipesComponent } from './recipes/recipes.component';
import { RecipeDetailComponent } from './recipes/recipe-detail/recipe-detail.component';
import { MainLogoComponent } from './main-logo/main-logo.component';
import { SearchUsersComponent } from './search-users/search-users.component';


const routes: Routes = [
    { path: "register", component: RegistrationComponent },
    { path: "Log-in", component: LogInComponent },
    { path: "search-page", component: SearchUsersComponent },
    { path: "updateDetails", component: DetailsComponent },
    { path: "addRecipe", component: AddRecipeComponent },
    {
        path: "My-Recipe", component: RecipesComponent, children: [
            { path: ':id', component: RecipeDetailComponent }
        ]
    },
    { path: "feed", component: MainLogoComponent },
    {
        path: "SearchByRecipeName", component: RecipesComponent, children: [
            { path: ':id', component: RecipeDetailComponent }
        ]
    },
    { path: ':id', component: RecipeDetailComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}

