const API_URL = "/api/products";

export const addProduct = async (productName) => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productName }),
    });
    if (!response.ok) {
        throw new Error("Failed to add product");
    }
    return await response.json();
};

export const getProductByName = async (productName) => {
    const response = await fetch(`${API_URL}/${encodeURIComponent(productName)}`);
    return response.ok ? response.json() : null;
};

export const updateProduct = async (productName, newName) => {
    const response = await fetch(`${API_URL}/${encodeURIComponent(productName)}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productName: newName }),
    });
    return response.json();
};

export const deleteProduct = async (productName) => {
    await fetch(`${API_URL}/${encodeURIComponent(productName)}`, {
        method: "DELETE"
    });
};

export const getAllProducts = async () => {
    const response = await fetch(`${API_URL}`, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    });
    if (!response.ok) {
        throw new Error("Failed to fetch products");
    }
    return await response.json();
};