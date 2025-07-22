import React, { useState } from "react";

const IngredientForm = () => {
    const [productName, setProductName] = useState("");
    const [amount, setAmount] = useState("");
    const [unit, setUnit] = useState("G");
    const [message, setMessage] = useState(null);

    const unitOptions = ["g","dag","kg","oz","lb","st","ml","cl","dl","l","tsp","tbsp","cup","pt","qt","gal","pc","sl","pn"];

    const handleSubmit = async (e) => {
        e.preventDefault();

        const requestData = {
            productName: { name: productName },
            amount: parseFloat(amount),
            unit: unit,
        };

        try {
            const response = await fetch("/api/ingredients", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestData),
            });

            const data = await response.json();
            setMessage(`Ingredient added! Received: ${JSON.stringify(data)}`);
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <form
            onSubmit={handleSubmit}
        >
            <label>
                <input
                    type="text"
                    value={productName}
                    onChange={(e) => setProductName(e.target.value)}
                    placeholder="Product Name"
                    style={{ padding: "8px", fontSize: "16px" }}
                    required
                />
            </label>

            <label>
                <input
                    type="number"
                    step="any"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    placeholder="Amount"
                    style={{ padding: "8px", fontSize: "16px" }}
                    required
                />
            </label>

            <label>
                <select
                    value={unit}
                    onChange={(e) => setUnit(e.target.value)}
                    style={{ padding: "8px", fontSize: "16px", backgroundColor: "gray", marginRight: "10px"}}
                >
                    {unitOptions.map((option) => (
                        <option key={option} value={option}>
                            {option}
                        </option>
                    ))}
                </select>
            </label>

            <button
                type="submit"
            >
                Submit
            </button>

            {message && <p>{message}</p>}
        </form>
    );
};

export default IngredientForm;