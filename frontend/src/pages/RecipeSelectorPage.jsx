import {Link, useLocation } from "react-router-dom";
import SearchAndAddRecipe from "../components/SearchAndAddRecipe.jsx";
import React from "react";

export default function RecipeSelectorPage() {
    const location = useLocation();
    const { listName, defaultPortions } = location.state || {};

    return (
        <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
            <h3 className="text-3xl font-bold text-[#c0a060]">Add Recipe to List: <span className="text-gray-400">{listName}</span></h3>

            <SearchAndAddRecipe listName={listName} defaultPortions={defaultPortions} />
            <Link
                to="/shoppingList"
                className="px-4 py-2 text-white rounded hover:bg-gray-700 transition-colors"
            >
                ← Back to List Manager
            </Link>
        </div>

    );
}