<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Product</title>
    <link rel="stylesheet" type="text/css" href="/static/index.css">
</head>
<body id="app">
<div><h3>Products:</h3></div>
<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Description</th>
        <th>Cart</th>
    </tr>

<%--
    <c:forEach items="${products}" var="product">
        <tr>
            <td><c:out value="${product.id}"/></td>
            <td><c:out value="${product.name}"/></td>
            <td><c:out value="${product.price}"/></td>
            <td><c:out value="${product.stock}"/></td>
            <td><c:out value="${product.description}"/></td>
            <td>
                <button v-on:click="addCart('<c:out value="${product.id}"/>', <c:out value="${product.stock}"/>);">Add to Cart</button>
            </td>
        </tr>
    </c:forEach>
--%>
    <tr v-for="product in stocks">
        <td>{{ product.id }}</td>
        <td>{{ product.name }}</td>
        <td class="price">{{ product.price | price }}</td>
        <td class="price">{{ product.stock }}</td>
        <td>{{ product.description }}</td>
        <td>
            <button v-on:click="addCart(product.id, product.stock);">Add to Cart</button>
        </td>
    </tr>

</table>
<div><h3>Cart:</h3></div>
<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Quantity</th>
    </tr>
    <tr v-for="product in cart">
        <td>{{ product.id }}</td>
        <td>{{ product.name }}</td>
        <td class="price">{{ product.price | price }}</td>
        <td class="price">{{ product.quantity }}</td>
    </tr>
    <template v-if="cart.length > 0">
    <tr>
        <th colspan="2">Total:</th>
        <th class="price">{{ totalPrice | price }}</th>
        <th class="price">{{ totalQuantity }}</th>
    </tr>
    </template>
</table>
<button v-on:click="clear">Clear</button>
<button v-on:click="checkout">Checkout</button>
<script src="static/vue.js"></script>
<script src="static/vue-resource.min.js"></script>
<script src="static/index.js"></script>
</body>
</html>
