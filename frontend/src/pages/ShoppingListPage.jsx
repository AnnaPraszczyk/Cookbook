import React, { useEffect, useState } from "react";
import {getAllLists} from "../api/recipeListApi";
import CreateListPanel from "../components/CreateListPanel";
import RecentListsPanel from "../components/RecentListsPanel";

export default function ShoppingListPage() {
    const [recentLists, setRecentLists] = useState([]);
    const [listsError, setListsError] = useState(null);

    useEffect(() => {
        (async () => {
            try {
                const data = await getAllLists();
                setRecentLists(data.slice().reverse());
            } catch (e) {
                setListsError("Failed to load recent lists.");
                console.error(e);
            }
        })();
    }, []);

    return (
        <div className="p-6 space-y-6 max-w-4xl mx-auto text-white">
            <h1 className="text-3xl font-bold mt-6">Shopping List Management</h1>
            <CreateListPanel />
            <RecentListsPanel recentLists={recentLists} error={listsError} />
        </div>
    );
}