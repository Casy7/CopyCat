


# Структура проєкту


```text
📦 CopyCat
┣ 📂 docs                      # 📚 Документація проєкту
┃ ┣ 📂 assets                  # Медіафайли для документації
┃ ┃ ┗ 📂 images                # Спільна папка для зображень (схеми, скріншоти для README)
┃ ┣ 📜 api.md                  # Опис REST API та WebSocket івентів
┃ ┣ 📜 architecture.md         # Опис архітектури (як працює сесія, очищення пам'яті)
┃ ┗ 📜 setup.md                # Інструкція з локального розгортання проєкту
┣ 📂 src                       # 💻 Вихідний код застосунку
┃ ┣ 📂 main
┃ ┃ ┣ 📂 java                  # Java класи (Spring Boot)
┃ ┃ ┃ ┗ 📂 com.copycat.app
┃ ┃ ┃   ┣ 📂 config            # Налаштування (WebSocket, CORS, WebMvcConfigurer)
┃ ┃ ┃   ┣ 📂 controller        # REST-контролери (для файлів) та WebSocket-контролери (для чату)
┃ ┃ ┃   ┣ 📂 model             # Сутності (Message, ChatRoom, UserSession)
┃ ┃ ┃   ┣ 📂 service           # Бізнес-логіка (генерація кодів, збереження файлів, кукі)
┃ ┃ ┃   ┗ 📜 CopyCatApplication.java # Точка входу в застосунок
┃ ┃ ┗ 📂 resources             # Статичні файли та конфігурації
┃ ┃   ┣ 📂 static              # Фронтенд (HTML, CSS, JS клієнт для чату)
┃ ┃   ┣ 📂 templates           # Шаблони (якщо будеш юзати Thymeleaf)
┃ ┃   ┗ 📜 application.yml     # Налаштування Spring Boot (порти, ліміти розміру файлів до 20MB)
┃ ┗ 📂 test                    # 🧪 Тести (Unit та Integration)
┣ 📜 .gitignore                # Файли, що ігноруються гітом (наприклад, завантажені файли юзерів)
┣ 📜 build.gradle              # Залежності проєкту (якщо використовуєш Gradle) / або pom.xml для Maven
┣ 📜 Dockerfile                # Скрипт для контейнеризації (знадобиться для деплою)
┗ 📜 README.md                 # Головна сторінка репозиторію (короткий опис, бейджі, посилання на docs/)
```