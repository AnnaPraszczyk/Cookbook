import React, { useEffect, useState } from 'react';
import { getShoppingList } from '../api/recipeListApi';
import { useParams } from 'react-router-dom';

export default function ShoppingListPage() {
    const { listName } = useParams();
    const [items, setItems] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const load = async () => {
            try {
                const data = await getShoppingList(listName);
                setItems(data);
            } catch (err) {
                console.error('Error while downloading shopping list:', err);
            } finally {
                setLoading(false);
            }
        };
        load();
    }, [listName]);

    if (loading) return <p>Loading...</p>;

    return (
        <div style={{ padding: '1rem' }}>
            <h2>Shopping list for: <em>{listName}</em></h2>
            {Object.keys(items).length === 0 ? (
                <p>No products on your shopping list.</p>
            ) : (
                <ul>
                    {Object.entries(items).map(([item, qty]) => (
                        <li key={item}>
                            {item}: {qty}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}