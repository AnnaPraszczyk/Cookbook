import Header from "./components/Header";
import Navigation from "./components/Navigation";
import './App.css'
import {Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Recipes from "./pages/Recipes";
import Products from "./pages/Products";
import Ingredients from "./pages/Ingredients";
import CreateRecipePage from "./pages/CreateRecipePage";
import UpdateRecipePage from "./pages/UpdateRecipePage";
import DeleteRecipePage from "./pages/DeleteRecipePage";
import ShoppingListPage from "./pages/ShoppingListPage";
import RecipeDetailsPage from "./pages/RecipeDetailsPage.jsx";
import RecipeSelectorPage from "./pages/RecipeSelectorPage.jsx";
import RecipeListViewPage from "./pages/RecipeListViewPage.jsx";


const App = () => {
    return (
        <>
            <Header/>
            <Navigation/>
            <main className="pt-24">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/products" element={<Products />} />
                    <Route path="/ingredients" element={<Ingredients />} />
                    <Route path="/recipes" element={<Recipes />} />
                    <Route path="/recipes/create" element={<CreateRecipePage />} />
                    <Route path="/recipes/:id" element={<RecipeDetailsPage />} />
                    <Route path="/recipes/update/:id" element={<UpdateRecipePage />} />
                    <Route path="/recipes/delete/:id" element={<DeleteRecipePage />} />
                    <Route path="/shoppingList" element={<ShoppingListPage />} />
                    <Route path="/shoppingList/:listName" element={<ShoppingListPage />} />
                    <Route path="/lists/:listName/select-recipes" element={<RecipeSelectorPage />} />
                    <Route path="/lists/:listName/view" element={<RecipeListViewPage />}/>
                </Routes>
            </main>
        </>
    );
};

export default App
