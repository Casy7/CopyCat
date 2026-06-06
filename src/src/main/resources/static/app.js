let stompClient = null;
let currentRoomCode = null;

// ==========================================
// 1. NAVIGATION AND ROOM CREATION
// ==========================================

async function createRoom() {
    try {
        // Make a POST request to our backend
        const response = await fetch('/api/v1/rooms', { method: 'POST' });
        const data = await response.json();

        // Got the room code - entering it immediately
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

    // Switch screens
    document.getElementById('landing-page').classList.add('hidden');
    document.getElementById('chat-page').classList.remove('hidden');
    document.getElementById('room-id-display').innerText = roomCode;
    document.getElementById('chat-box').innerHTML = ''; // Clear chat

    // Load old messages and connect to the socket
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
// 2. LOADING HISTORY (REST API)
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
// 3. WEBSOCKET (REAL-TIME MAGIC)
// ==========================================

function connectWebSocket() {
    // Specify our WebSocket address (from ChatController)
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;

    // Form the correct address
    const socket = new WebSocket(protocol + '//' + host + '/ws');
    stompClient = Stomp.over(socket);

    // Disable unnecessary console logs
    stompClient.debug = null;

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        // Subscribe to our room's channel
        stompClient.subscribe('/topic/room/' + currentRoomCode, function (messageOutput) {
            // When someone sends a message, we receive it here
            const msg = JSON.parse(messageOutput.body);
            showMessage(msg.content, msg.timestamp);
        });
    });
}

function sendMessage() {
    const input = document.getElementById('message-input');
    const content = input.value.trim();

    if (content && stompClient) {
        // Send a message to the server via STOMP
        const request = { content: content };
        stompClient.send(`/app/room/${currentRoomCode}/send`, {}, JSON.stringify(request));

        input.value = ''; // Clear input field
    }
}

// Handle Enter key press
function handleEnter(event) {
    if (event.key === 'Enter') sendMessage();
}

// ==========================================
// 4. ON-SCREEN DISPLAY
// ==========================================

function showMessage(content, timestamp) {
    const chatBox = document.getElementById('chat-box');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');

    // Format time
    const date = new Date(timestamp);
    const timeString = date.getHours().toString().padStart(2, '0') + ':' +
        date.getMinutes().toString().padStart(2, '0');

    messageElement.innerHTML = `
        <span class="text">${content}</span>
        <span class="time">${timeString}</span>
    `;

    chatBox.appendChild(messageElement);
    // Automatically scroll down to the latest message
    chatBox.scrollTop = chatBox.scrollHeight;
}

// ==========================================
// 5. WORKING WITH FILES
// ==========================================

async function uploadFile() {
    const fileInput = document.getElementById('file-input');
    const file = fileInput.files[0];

    if (!file) return;

    // Create a form for file submission
    const formData = new FormData();
    formData.append('file', file);

    try {
        // Send the file to our new endpoint
        const response = await fetch('/api/v1/files/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error('Помилка завантаження');

        const data = await response.json();

        // Once the file is uploaded, send a message to the chat with a link to it
        const fileMessage = `📁 [ФАЙЛ] <a href="${data.fileUrl}" target="_blank" style="color: #ffff00;">${data.originalName}</a>`;

        // Send via WebSocket
        if (stompClient) {
            const request = { content: fileMessage };
            stompClient.send(`/app/room/${currentRoomCode}/send`, {}, JSON.stringify(request));
        }

        fileInput.value = ''; // Clear input
    } catch (error) {
        console.error(error);
        alert('Помилка при завантаженні файлу. Можливо він завеликий?');
    }
}