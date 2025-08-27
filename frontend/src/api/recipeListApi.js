import axios from 'axios';
const API = '/api/lists';

export const createRecipeList = async ({ listName, listDescription }) => {
    await axios.post(`${API}`, { listName, listDescription});
};

export const addRecipeToList = async ({ listName, recipeId, portions }) => {
    console.log("📤 Sending to backend:", { listName, recipeId, portions });
    await axios.post(`${API}/${listName}/recipes`, { recipeId, listName, portions , confirm: true,listDescription:"" });
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

export const searchRecipes = async ({ recipeName, category, page = 0, size = 10 }) => {
    const params = { page, size };
    if (recipeName && recipeName.trim()) params.recipeName = recipeName.trim();
    if (category && category.trim()) params.category = category.trim();
    if (!params.recipeName && !params.category) {
        throw new Error("Please provide a recipe name or select a category.");
    }
    const { data } = await axios.get("/api/recipes/search", { params });
    return data;
};

export async function deleteRecipeFromList({ listName, entryId }) {
    try {
        await axios.delete(`${API}/${listName}/entries/${entryId}`);
    } catch (err) {
        console.error("API deleteRecipeFromList error:", err);
        throw new Error("Failed to delete recipe from list");
    }
}