new Vue({
    el: '#app',
    data: {
        stocks: [],
        cart: [],
    },

    ready: function () {
        this.$http.get('/list').then(function (response) {
            this.setResult(response);
        });
    },

    methods: {
        addCart: function (id) {
            this.$http.post('/cart', {productId: id}).then(function (response) {
                this.setResult(response);
            });
        },

        clear: function () {
            this.$http.post('/clear').then(function (response) {
                this.setResult(response);
            });
        },

        checkout: function () {
            this.$http.post('/checkout').then(function (response) {
                this.setResult(response);
            });
        },

        setResult: function (res) {
            var v = res.body;
            if (v != null) {
                this.stocks = v.stocks;
                this.cart = v.cart;
                if (v.messages != null) {
                    for (var i in v.messages) {
                        alert(v.messages[i]);
                    }
                }
            }
        }
    },

    computed: {
        totalPrice: function () {
            var sum = 0;
            if (this.cart == null) return sum;
            for (var i in this.cart) {
                sum += this.cart[i].price * this.cart[i].quantity;
            }
            return sum;
        },

        totalQuantity: function () {
            var sum = 0;
            if (this.cart == null) return sum;
            for (var i in this.cart) {
                sum += this.cart[i].quantity;
            }
            return sum;
        }
    },

});
Vue.http.options.emulateJSON = true;
Vue.filter("price", function (value) {
    if (value == null)
        return "0.0";
    return value + ".0";
});