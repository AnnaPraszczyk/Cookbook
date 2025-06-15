import { useState } from "react";
import { addProduct } from "../api";

const ProductForm = ({ onProductAdded }) => {
    const [productName, setProductName] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (productName.trim() === "") return;
        await addProduct(productName);
        onProductAdded();
        setProductName("");
    };

    return (
        <form onSubmit={handleSubmit} className="product-form">
            <input
                type="text"
                placeholder="Product Name"
                value={productName}
                onChange={(e) => setProductName(e.target.value)}/>
            <button type="submit">Add Product</button>
        </form>
    );
};

export default ProductForm;