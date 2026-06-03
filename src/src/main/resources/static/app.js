let stompClient = null;
let currentRoomCode = null;

// ==========================================
// 1. НАВІГАЦІЯ ТА СТВОРЕННЯ КІМНАТ
// ==========================================

async function createRoom() {
    try {
        // Робимо POST запит на наш бекенд
        const response = await fetch('/api/v1/rooms', { method: 'POST' });
        const data = await response.json();
        
        // Отримали код кімнати - відразу заходимо в неї
        enterRoom(data.roomCode);
    } catch (error) {
        console.error('Помилка створення кімнати:', error);
        alert('Не вдалося створити кімнату!');
    }
}

function joinRoomFromInput() {
    const code = document.getElementById('room-code-input').value.trim();
    if (code.length > 0) {
        enterRoom(code);
    }
}

async function enterRoom(roomCode) {
    currentRoomCode = roomCode;
    
    // Перемикаємо екрани
    document.getElementById('landing-page').classList.add('hidden');
    document.getElementById('chat-page').classList.remove('hidden');
    document.getElementById('room-id-display').innerText = roomCode;
    document.getElementById('chat-box').innerHTML = ''; // Очищуємо чат

    // Завантажуємо старі повідомлення та підключаємось до сокету
    await loadHistory();
    connectWebSocket();
}

function leaveRoom() {
    if (stompClient) {
        stompClient.disconnect();
    }
    currentRoomCode = null;
    document.getElementById('chat-page').classList.add('hidden');
    document.getElementById('landing-page').classList.remove('hidden');
}

// ==========================================
// 2. ЗАВАНТАЖЕННЯ ІСТОРІЇ (REST API)
// ==========================================

async function loadHistory() {
    try {
        const response = await fetch(`/api/v1/rooms/${currentRoomCode}/messages`);
        if (!response.ok) throw new Error('Кімнати не існує');
        
        const messages = await response.json();
        messages.forEach(msg => showMessage(msg.content, msg.timestamp));
    } catch (error) {
        console.error(error);
        alert('Кімнату не знайдено!');
        leaveRoom();
    }
}

// ==========================================
// 3. WEBSOCKET (МАГІЯ РЕАЛЬНОГО ЧАСУ)
// ==========================================

function connectWebSocket() {
    // Вказуємо адресу нашого WebSocket (з ChatController)
    const socket = new WebSocket('ws://' + window.location.host + '/ws');
    stompClient = Stomp.over(socket);

    // Вимикаємо зайві логи в консолі
    stompClient.debug = null; 

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // Підписуємось на канал нашої кімнати
        stompClient.subscribe('/topic/room/' + currentRoomCode, function (messageOutput) {
            // Коли хтось кидає повідомлення, ми його отримуємо тут
            const msg = JSON.parse(messageOutput.body);
            showMessage(msg.content, msg.timestamp);
        });
    });
}

function sendMessage() {
    const input = document.getElementById('message-input');
    const content = input.value.trim();

    if (content && stompClient) {
        // Відправляємо повідомлення на сервер через STOMP
        const request = { content: content };
        stompClient.send(`/app/room/${currentRoomCode}/send`, {}, JSON.stringify(request));
        
        input.value = ''; // Очищуємо поле вводу
    }
}

// Обробка натискання Enter
function handleEnter(event) {
    if (event.key === 'Enter') sendMessage();
}

// ==========================================
// 4. ВІДОБРАЖЕННЯ НА ЕКРАНІ
// ==========================================

function showMessage(content, timestamp) {
    const chatBox = document.getElementById('chat-box');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    
    // Форматуємо час
    const date = new Date(timestamp);
    const timeString = date.getHours().toString().padStart(2, '0') + ':' + 
                       date.getMinutes().toString().padStart(2, '0');

    messageElement.innerHTML = `
        <span class="text">${content}</span>
        <span class="time">${timeString}</span>
    `;
    
    chatBox.appendChild(messageElement);
    // Автоматично скролимо вниз до найновішого повідомлення
    chatBox.scrollTop = chatBox.scrollHeight;
}

// ==========================================
// 5. РОБОТА З ФАЙЛАМИ
// ==========================================

async function uploadFile() {
    const fileInput = document.getElementById('file-input');
    const file = fileInput.files[0];
    
    if (!file) return;

    // Створюємо форму для відправки файлу
    const formData = new FormData();
    formData.append('file', file);

    try {
        // Відправляємо файл на наш новий ендпоінт
        const response = await fetch('/api/v1/files/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error('Помилка завантаження');

        const data = await response.json();
        
        // Коли файл завантажено, відправляємо в чат повідомлення з посиланням на нього
        const fileMessage = `📁 [ФАЙЛ] <a href="${data.fileUrl}" target="_blank" style="color: #ffff00;">${data.originalName}</a>`;
        
        // Відправляємо через веб-сокет
        if (stompClient) {
            const request = { content: fileMessage };
            stompClient.send(`/app/room/${currentRoomCode}/send`, {}, JSON.stringify(request));
        }

        fileInput.value = ''; // Очищуємо інпут
    } catch (error) {
        console.error(error);
        alert('Помилка при завантаженні файлу. Можливо він завеликий?');
    }
}