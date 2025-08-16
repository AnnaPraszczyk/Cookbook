import axios from 'axios';
const API = '/api/lists';

export const createRecipeList = async ({ listName, listDescription }) => {
    await axios.post(`${API}`, { listName, listDescription});
};

export const addRecipeToList = async ({ listName, recipeId, portions }) => {
    await axios.post(`${API}/${listName}/recipes`, { recipeId, portions });
};

export const getRecipesList = async (listName) => {
    const res = await axios.get(`${API}/${listName}`);
    return res.data;
};

export const removeRecipe = async (listName, recipeId) => {
    await axios.delete(`${API}/${listName}/recipes/${recipeId}`);
};

export const deleteList = async (listName) => {
    await axios.delete(`${API}/${listName}`);
};

export const getShoppingList = async(listName) =>{
    try {
        const res = await axios.get(`${API}/${listName}/shopping`);
        return res.data;
    } catch (err) {
        throw new Error("Failed to fetch shopping list");
    }
}
export const clearList = async (listName, confirm = true) => {
    const res = await axios.delete(`${API}/${listName}/clear?confirm=${confirm}`);
    return res.data;
};

export const getAllLists = async () => {
    try {
        const res = await axios.get(`${API}`);
        return res.data;
    } catch (err) {
        throw new Error("Failed to fetch lists");
    }
};

export const searchRecipes = async ({ name, category }) => {
    const params = new URLSearchParams();
    if (name && !category) {
        params.append("name", name);
    } else if (category && !name) {
        params.append("category", category);
    } else {
        throw new Error("Provide either name or category");
    }
    const res = await axios.get(`/api/recipes/search?${params.toString()}`);
    return res.data;
};

export async function deleteRecipeFromList({ listName, entryId }) {
    try {
        await axios.delete(`${API}/${listName}/entries/${entryId}`);
    } catch (err) {
        console.error("API deleteRecipeFromList error:", err);
        throw new Error("Failed to delete recipe from list");
    }
}