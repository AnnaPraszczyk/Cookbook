import axios from 'axios';

const API = '/api/recipes/lists';

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

export async function getShoppingList(listName) {
    const response = await fetch(`/api/recipes/lists/${listName}/shopping`);
    if (!response.ok) {
        throw new Error("Failed to fetch shopping list");
    }
    return await response.json();
}
export const clearList = async (listName, confirm = true) => {
    const res = await axios.delete(`${API}/${listName}/clear?confirm=${confirm}`);
    return res.data;
};
