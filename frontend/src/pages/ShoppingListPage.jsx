import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
    getShoppingList,
    clearList,
    getAllLists
} from "../api/recipeListApi";

import CreateListPanel from "../components/CreateListPanel";
import RecentListsPanel from "../components/RecentListsPanel";

export default function ShoppingListPage() {
    const { listName } = useParams();
    const [items, setItems] = useState({});
    const [loading, setLoading] = useState(true);
    const [recentLists, setRecentLists] = useState([]);
    const [listsError, setListsError] = useState(null);


    useEffect(() => {
        const load = async () => {
            if (!listName || listName === "undefined") return;
            setLoading(true);
            try {
                const data = await getShoppingList(listName);
                setItems(data || {});
            } catch (err) {
                console.error("Failed to load shopping list:", err);
            } finally {
                setLoading(false);
            }
        };
        load();
    }, [listName]);


    useEffect(() => {
        const fetchLists = async () => {
            try {
                const data = await getAllLists();
                setRecentLists(data.slice().reverse());
            } catch (e) {
                setListsError("Failed to load recent lists.");
                console.error(e);
            }
        };
        fetchLists();
    }, []);


    const handleClear = async () => {
        const confirmed = window.confirm("Are you sure you want to clear the list?");
        if (!confirmed) return;
        try {
            await clearList(listName);
            setItems({});
        } catch (err) {
            console.error("Failed to clear list:", err);
        }
    };

    return (
        <div className="p-6 space-y-6 max-w-4xl mx-auto text-white">
            <h1 className="text-3xl font-bold">Shopping List Management</h1>

            <CreateListPanel />
            <RecentListsPanel recentLists={recentLists} error={listsError} />
        </div>
    );
}