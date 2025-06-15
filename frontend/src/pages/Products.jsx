import ProductForm from "../components/ProductForm";
import ProductList from "../components/ProductList";

const Products = () => {
    return (
        <div style={{textAlign:"center"}}>
            <h1>Product Management</h1>
            <ProductForm />
            <ProductList />
        </div>
    );
};

export default Products;