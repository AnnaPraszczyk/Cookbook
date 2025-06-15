const API_URL = "http://localhost:8080/products";

export const addProduct = async (productName) => {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productName }),
    });
    return response.json();
};

export const getProductByName = async (productName) => {
    const response = await fetch(`${API_URL}/${productName}`);
    return response.ok ? response.json() : null;
};

export const updateProduct = async (productName, newName) => {
    const response = await fetch(`${API_URL}/${productName}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productName: newName }),
    });
    return response.json();
};

export const deleteProduct = async (productName) => {
    await fetch(`${API_URL}/${productName}`, { method: "DELETE" });
};