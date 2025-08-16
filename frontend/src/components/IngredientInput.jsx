import React, { useState, useEffect } from "react";

const IngredientInput = ({ onAdd, productOptions = [], resetCount }) => {
    const [name, setName]   = useState("");
    const [amt, setAmt]     = useState("");
    const [unit, setUnit]   = useState("g");
    const units = ["g","dag","kg","oz","lb","st","ml","cl","dl","l","tsp","tbsp","cup","pt","qt","gal","pc","sl","pn"];
    const [customName, setCustomName] = useState("");
    useEffect(() => {
            setName("");
            setAmt("");
            setUnit("g");
            setCustomName("");
    }, [resetCount]);

    const add = e => {
        e.preventDefault();
        if (!name.trim() || !amt.trim()) return;
        const parsedAmount = parseFloat(amt);
        if (isNaN(parsedAmount) || parsedAmount <= 0) return;
        const finalName = name === "__custom__" ? customName : name;
        if (!finalName.trim() || !amt.trim()) return;
        onAdd({
            productName: finalName,
            amount: +amt,
            unit });
        setName("");
        setAmt("");
        setUnit("g");
    };

    return (
        <div className="flex flex-wrap sm:flex-row items-end gap-3">
            <select
                value={name}
                onChange={e => {
                    const selected = e.target.value;
                    setName(selected);
                    if (selected === "__custom__") {
                        setCustomName("");
                    }
                }}
                className="p-2 w-64 h-12 border-2 text-lg border-gray-400 rounded bg-[#292F33] text-gray-400">
                <option value="">Select product</option>
                {productOptions
                    .map((p, i) => (
                        <option key={i} value={p.productName}>{p.productName}</option>
                ))}
                <option value="__custom__">Other...</option>
            </select>
            { name === "__custom__" && (
                <input
                    value={customName}
                    onChange={e => setCustomName(e.target.value)}
                    placeholder="Enter new product name"
                    className="mt-2 p-2 w-64 border-2 text-lg border-gray-400 rounded bg-[#292F33] text-gray-400"
                />
            )}
            <input
                type="number"
                min="0"
                value={amt}
                onChange={e=>setAmt(e.target.value)}
                placeholder="Amount"
                className="w-24 p-2 border-2 rounded text-lg bg-[#292F33]  text-gray-400 border-gray-400"
            />
            <select
                value={unit}
                onChange={e=>setUnit(e.target.value)}
                className="w-16 p-2 border-2 border-gray-400 rounded text-lg bg-[#292F33] text-gray-400 h-12 ">
                {units.map(u=> <option key={u} value={u}>{u}</option>)}
            </select>
            <button
                onClick={add}
                className="p-2 w-28 bg-[#c0a060] text-white text-lg rounded h-12 hover:bg-gray-600 transition-colors duration-200">
                Add
            </button>
        </div>
    );
};
export default IngredientInput;
