import Header from "./components/Header";
import Navigation from "./components/Navigation";
import './App.css'
import {Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Recipes from "./pages/Recipes";
import Products from "./pages/Products";
import CreateRecipePage from "./pages/CreateRecipePage";
import UpdateRecipePage from "./pages/UpdateRecipePage";
import DeleteRecipePage from "./pages/DeleteRecipePage";
import ShoppingListPage from "./pages/ShoppingListPage";
import RecipeDetailsPage from "./pages/RecipeDetailsPage.jsx";
import RecipeSelectorPage from "./pages/RecipeSelectorPage.jsx";
import RecipeListViewPage from "./pages/RecipeListViewPage.jsx";
import CreateListForm from "./components/CreateListForm";


const App = () => {
    return (
        <>
            <Header/>
            <Navigation/>
            <main className="pt-24">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/products" element={<Products />} />
                    <Route path="/recipes/search" element={<Recipes />} />
                    <Route path="/recipes" element={<Recipes />} />
                    <Route path="/recipes/create" element={<CreateRecipePage />} />
                    <Route path="/recipes/:recipeId" element={<RecipeDetailsPage />} />
                    <Route path="/recipes/update/:recipeId" element={<UpdateRecipePage />} />
                    <Route path="/recipes/delete/:recipeId" element={<DeleteRecipePage />} />
                    <Route path="/shoppingList" element={<ShoppingListPage />} />
                    <Route path="/shoppingList/:listName" element={<ShoppingListPage />} />
                    <Route path="/lists/:listName/select-recipes" element={<RecipeSelectorPage />} />
                    <Route path="/lists/:listName/view" element={<RecipeListViewPage />}/>
                    <Route path="/lists/create" element={<CreateListForm />} />
                </Routes>
            </main>
        </>
    );
};

export default App
