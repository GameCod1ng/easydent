// JavaScript 기능 코드
const reservationSection = document.getElementById("reservation-section");
const dentInfoSection = document.getElementById("dent-info-section");
const appointmentSection = document.getElementById("appointment-section");
const confirmButton = document.getElementById("confirm-button");
const timeSlots = document.getElementById("time-slots");
let selectedDate = null; // 선택된 날짜
let selectedTime = null; // 선택된 시간

// 예약 날짜 선택 이벤트
document.getElementById("reservation-date").addEventListener("change", (event) => {
    selectedDate = event.target.value; // 선택된 날짜 저장
    validateReservation(); // 예약 버튼 활성화 여부 확인
});

// 섹션 전환 함수
function showSection(sectionId) {
    document.querySelectorAll('.container').forEach(section => section.classList.remove("active"));
    document.getElementById(sectionId).classList.add("active");
}

// 뒤로가기 버튼 처리
function goBack() {
    const activeSection = document.querySelector('.container.active').id;
    if (activeSection === "reservation-section") {
        window.location.href = "/";
    } else {
        window.history.back();
    }
}

window.addEventListener("popstate", (event) => {
    const section = event.state?.section;
    if (section) {
        showSection(section);
    } else if (document.querySelector('.container.active').id !== "reservation-section") {
        window.location.href = "/";
    }
});

// 치과 검색
function searchDentistry() {
    const reservationList = document.getElementById("reservation-list");
    const searchInput = document.getElementById("search");
    const keyword = searchInput.value.trim();
    fetch(`/reservation/search?keyword=${encodeURIComponent(keyword)}`)
        .then(response => response.json())
        .then(data => {
            reservationList.innerHTML = '';
            if (data && data.length > 0) {
                data.forEach(dentistry => {
                    const listItem = document.createElement('div');
                    listItem.className = 'item';
                    listItem.innerHTML = `
                            <div class="item-text">
                                <strong>${dentistry.clinicName}</strong><br>
                                주소 : ${dentistry.address}<br>
                                진료시간 : ${dentistry.openAtweekday} - ${dentistry.closeAtweekday}
                            </div>
                            <button class="button" onclick="goToDentInfo('${dentistry.clinicName}', '${dentistry.address}', '${dentistry.telephone}', '${dentistry.openAtweekday}', '${dentistry.closeAtweekday}', '${dentistry.lunchTime}')">예약하기</button>
                        `;
                    reservationList.appendChild(listItem);
                });
            } else {
                reservationList.innerHTML = '<p>검색 결과가 없습니다.</p>';
            }
        })
        .catch(error => console.error('Error:', error))
        .finally(() => {
            searchInput.value = '';
        });
}

// 치과 정보로 이동
function goToDentInfo(name, address, phone, openTime, closeTime, lunchTime) {
    document.getElementById("dent-name").textContent = name || "이름 없음";
    document.getElementById("dent-address").textContent = `주소: ${address || "주소 없음"}`;
    document.getElementById("dent-phone").textContent = `전화번호: ${phone || "전화번호 없음"}`;
    document.getElementById("dent-clinicTime").textContent = `진료시간: ${openTime || "09:00"} - ${closeTime || "18:00"}`;
    document.getElementById("dent-lunchTime").textContent = `점심시간: ${lunchTime || "13:00"}`;
    history.pushState({section: "dent-info-section"}, null, "");
    showSection("dent-info-section");
}

// 예약 시간 슬롯 생성
function generateTimeSlots() {
    timeSlots.innerHTML = "";
    const startTime = new Date();
    startTime.setHours(10, 0, 0, 0);
    const endTime = new Date();
    endTime.setHours(18, 0, 0, 0);

    while (startTime < endTime) {
        const slot = document.createElement("div");
        slot.className = "time-slot";
        slot.textContent = startTime.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
        slot.onclick = () => {
            document.querySelectorAll('.time-slot').forEach(s => s.classList.remove("selected"));
            slot.classList.add("selected");
            selectedTime = slot.textContent;
            validateReservation(); // 예약 버튼 활성화 여부 확인
        };
        timeSlots.appendChild(slot);
        startTime.setMinutes(startTime.getMinutes() + 30);
    }
}

// 예약 섹션으로 이동
function goToAppointment() {
    history.pushState({section: "appointment-section"}, null, "");
    showSection("appointment-section");
    generateTimeSlots();
}

// 예약 버튼 활성화 여부 확인
function validateReservation() {
    if (selectedDate && selectedTime) {
        confirmButton.disabled = false;
    } else {
        confirmButton.disabled = true;
    }
}

// 예약 확정
function confirmReservation() {
    if (selectedDate && selectedTime) {
        const reservationData = {
            clinicName: document.getElementById("dent-name").textContent,
            address: document.getElementById("dent-address").textContent.replace("주소: ", ""),
            telephone: document.getElementById("dent-phone").textContent.replace("전화번호: ", ""),
            reservedDate: selectedDate,
            reservedTime: selectedTime,
            inQuery: document.getElementById("content").value
        };

        fetch('http://localhost:9090/reservation/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reservationData),
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .then(data => {
                alert('예약이 성공적으로 저장되었습니다.');
                history.pushState({section: "reservation-section"}, null, "");
                showSection("reservation-section");
            })
            .catch(error => {
                console.error('Error:', error);
                alert('예약에 실패했습니다. 다시 시도해 주세요.');
            });
    } else {
        alert('예약 날짜와 시간을 선택하세요.');
    }
}

// 날짜 입력 필드에 최소 날짜 설정
const today = new Date();
const tomorrow = new Date(today);
tomorrow.setDate(tomorrow.getDate() + 1);
const minDate = tomorrow.toISOString().split('T')[0];
document.getElementById("reservation-date").min = minDate;

// 날짜 선택 이벤트 리스너 수정
document.getElementById("reservation-date").addEventListener("change", (event) => {
    const selectedDateValue = new Date(event.target.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (selectedDateValue < today) {
        alert("오늘 이후 날짜만 선택 가능합니다.");
        event.target.value = '';
        selectedDate = null;
    } else {
        selectedDate = event.target.value;
    }
    validateReservation();
});

history.replaceState({section: "reservation-section"}, null, "");
