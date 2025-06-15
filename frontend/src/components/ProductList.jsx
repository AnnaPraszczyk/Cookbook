import { useState } from "react";
import { getProductByName, deleteProduct } from "../api";

const ProductList = () => {
    const [productName, setProductName] = useState("");
    const [product, setProduct] = useState(null);

    const handleSearch = async () => {
        const foundProduct = await getProductByName(productName);
        setProduct(foundProduct);
    };

    const handleDelete = async () => {
        await deleteProduct(productName);
        setProduct(null);
    };

    return (
        <div>
            <input
                type="text"
                placeholder="Search Product"
                value={productName}
                onChange={(e) => setProductName(e.target.value)}
            />
            <button onClick={handleSearch}>Search</button>
            {product && (
                <div>
                    <p>ID: {product.productId}</p>
                    <p>Name: {product.productName}</p>
                    <button onClick={handleDelete}>Delete</button>
                </div>
            )}
        </div>
    );
};

export default ProductList;