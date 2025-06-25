import React, { useEffect, useState } from 'react';
import { getShoppingList } from '../api/recipeListApi';

export default function ShoppingListView({ listName }) {
    const [items, setItems] = useState({});
    const [error, setError] = useState(null);

    useEffect(() => {
        getShoppingList(listName)
            .then(setItems)
            .catch(err => {
                console.error(err);
                setError("Failed to load shopping list.");
            });
    }, [listName]);
    return (
        <div>
            <h4>Shopping list</h4>
            <ul>
                {Object.entries(items).map(([item, qty]) => (
                    <li key={item}>{item}: {qty}</li>
                ))}
            </ul>
        </div>
    );
}