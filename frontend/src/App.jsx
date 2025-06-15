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



const App = () => {
    return (
        <>
            <Header/>
            <Navigation/>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/products" element={<Products />} />
                <Route path="/ingredients" element={<Ingredients />} />
                <Route path="/recipes" element={<Recipes />} />
                <Route path="/recipes/create" element={<CreateRecipePage />} />
                <Route path="/recipes/update" element={<UpdateRecipePage />} />
                <Route path="/recipes/delete" element={<DeleteRecipePage />} />
            </Routes>
        </>
    );
};

export default App
