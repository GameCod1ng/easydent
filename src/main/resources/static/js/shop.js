document.addEventListener("DOMContentLoaded", () => {
    const productList = document.getElementById("product-list");

    fetch("http://192.168.45.179:9090/products") // API 호출
        .then(response => {
            if (!response.ok) {
                throw new Error("네트워크 응답에 문제가 있습니다.");
            }
            return response.json();
        })
        .then(data => {
            productList.innerHTML = ""; // 기존 콘텐츠 초기화
            data.forEach(product => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <img class="card-img-top" src="${product.imageUrl}" alt="제품 이미지">
                    <div class="card-body">
                        <p class="card-title">${product.productName}</p>
                        <br>
                        <p class="card-text">${product.price}원</p>
                        <button class="buy-btn" onclick="buyProduct('${product.id}')">구매하기</button>
                    </div>
                `;
                productList.appendChild(card);
            });
        })
        .catch(error => {
            console.error("제품 데이터를 불러오는 중 오류 발생:", error);
            productList.innerHTML = "<p>추천 제품을 불러오는 데 실패했습니다.</p>";
        });
});

function buyProduct(productId) {
    alert(`제품 ID: ${productId} 구매 완료`);
}
