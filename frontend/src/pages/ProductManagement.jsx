import { useState, useEffect } from "react";
import { getProductByName, deleteProduct, updateProduct, addProduct} from "../api";
import { useNavigate } from "react-router-dom";

const ProductManagement = () => {
    const [newProductName, setNewProductName] = useState("");
    const [searchProductName, setSearchProductName] = useState("");
    const [product, setProduct] = useState(null);
    const [editedName, setEditedName] = useState("");
    const [loading, setLoading] = useState(false);
    const [searchInitiated, setSearchInitiated] = useState(false);
    const navigate = useNavigate();
    const [message, setMessage] = useState(null);
    const [products, setProducts] = useState([]);

    useEffect(() => {
        if (message) {
            const timeout = setTimeout(() => setMessage(null), 4000);
            return () => clearTimeout(timeout);
        }
    }, [message]);

    const handleAdd = async () => {
        if (newProductName.trim() === "") return;
        try {
            const response = await addProduct(newProductName);
            const created = await response;
            setMessage({ text: "Product created!", type: "success" });
            setNewProductName("");
        } catch (err) {
            setMessage({ text: "Failed to add product.", type: "error" });
        }
    };

    const handleSearch = async () => {
        setLoading(true);
        setSearchInitiated(true);
        try {
            const found = await getProductByName(searchProductName);
            setProduct(found);
            setEditedName(found?.productName || "");
        } catch (err) {
            console.error("Search error:", err);
            setProduct(null);
        } finally {
            setLoading(false);
        }
    };

    const handleUpdate = async () => {
        if (!product || editedName.trim() === "") return;
      try{
        const updated = { ...product, productName: editedName };
        await updateProduct(product.productName, editedName);
        setProduct(updated);
        setMessage({ text: "Product updated!", type: "success" });
        } catch (err) {
        setMessage({ text: "Failed to update product", type: "error" });
        }
};

    const handleDelete = async () => {
        if (!product) return;
        try{
        await deleteProduct(product.productName);
        setProduct(null);
        setSearchProductName("");
            setMessage({ text: "Product deleted!", type: "success" });
        } catch (err) {
            setMessage({ text: "Failed to delete product", type: "error" });
        }
    };

    return (
        <div className="p-6 mt-0 space-y-6 max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold">Products Management</h1>

            <div className="flex gap-4 items-center">
                <input
                    type="text"
                    placeholder="Product Name"
                    value={newProductName}
                    onChange={(e) => setNewProductName(e.target.value)}
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"/>
                <button
                    onClick={handleAdd}
                    className="text-lg px-4 py-2 w-[150px] bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Add Product
                </button>
            </div>

            <div className="flex gap-4 items-center">
                <input
                    type="text"
                    placeholder="Search Product"
                    value={searchProductName}
                    onChange={(e) => setSearchProductName(e.target.value)}
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"/>
                <button
                    onClick={handleSearch}
                    className="text-lg px-4 py-2 w-[150px] bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Search
                </button>
            </div>
            {message && (
                <p
                    className={`text-lg ${
                        message.type === "success" ? "text-green-500" : "text-red-500"
                    }`}>
                    {message.text}
                </p>
            )}

            {/* RESULTS */}
            {loading && <p>Loading…</p>}
            {searchInitiated && !loading && !product && <p>No product found.</p>}
            {product && (
                <div className="bg-[#333] text-white p-4 rounded border-2 border-gray-400">
                    <p><strong>Product:</strong> {product.productName}</p>
                    <input
                        type="text"
                        value={editedName}
                        onChange={(e) => setEditedName(e.target.value)}
                        className="mt-4 p-2 text-lg bg-[#222] border-2 border-gray-400 rounded w-full text-gray-200"
                    />
                    <div className="flex gap-4 mt-4">
                        <button
                            onClick={handleUpdate}
                            className="text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200">

                        Update
                        </button>
                        <button
                            onClick={handleDelete}
                            className="text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                        Delete
                        </button>
                    </div>
                </div>
            )}

        </div>
    );
};

export default ProductManagement;