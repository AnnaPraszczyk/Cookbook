import axios from 'axios';

const API = '/api/lists';

export const createRecipeList = async (listName) => {
    await axios.post(API, { listName });
};

export const addRecipeToList = async (listName, recipeId) => {
    await axios.post(`${API}/${listName}/recipes`, { recipeId });
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
    const res = await axios.get(`${API}/${listName}/shopping`);
    if (!res.ok) {
        throw new Error("Failed to fetch shopping list");
    }
    return res.data;
}
export const clearList = async (listName, confirm = true) => {
    const res = await axios.delete(`${API}/${listName}/clear?confirm=${confirm}`);
    return res.data;
};

export const getRecipesByQuery = async (name, category) => {
    const params = new URLSearchParams();
    if (name) params.append("name", name);
    if (category) params.append("category", category);
    const res = await fetch(`/api/recipes/search?${params}`);
    return res.ok ? res.json().then((r) => r.content) : [];
};

export const getAllLists = async () => {
    const res = await fetch(`${API}`);
    if (!res.ok) throw new Error("Failed to load recipe lists");
    return await res.json();
};