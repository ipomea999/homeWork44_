<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Информация о книге</title>
    <link rel="stylesheet" href="/css/freemarker.css">
</head>
<body>
<div class="container">
    <h1>Детали книги</h1>
    <p><a href="/books">← Назад к списку книг</a></p>

    <div style="display: flex; gap: 20px; margin-top: 20px;">
        <div>
            <img src="/images/${book.image!'1.jpg'}" alt="${book.title}" style="width: 200px; border: 1px solid #ccc; border-radius: 5px;">
        </div>
        <div>
            <h2>${book.title}</h2>
            <p><strong>Автор:</strong> ${book.author}</p>
            <p><strong>Описание:</strong> ${book.description!'Описание отсутствует'}</p>
            <p><strong>Текущий статус:</strong>
                <#if book.borrowed>
                <span style="color: red; font-weight: bold;">Выдана</span>
                <#if book.currentHolder??>
                сотруднику <a href="/employee/${book.currentHolder.id}">${book.currentHolder.name}</a>
            </#if>
            <#else>
            <span style="color: green; font-weight: bold;">Доступна в библиотеке</span>
        </#if>
        </p>

        <#if user??>
        <div style="margin-top: 20px;">
            <#if canBorrow>
            <form action="/books/borrow" method="post">
                <input type="hidden" name="bookId" value="${book.id}">
                <button type="submit" style="padding: 10px 15px; background-color: #4CAF50; color: white; border: none; border-radius: 3px; cursor: pointer;">Взять книгу</button>
            </form>
            <#elseif canReturn>
            <form action="/books/return" method="post">
                <input type="hidden" name="bookId" value="${book.id}">
                <button type="submit" style="padding: 10px 15px; background-color: #f44336; color: white; border: none; border-radius: 3px; cursor: pointer;">Вернуть книгу</button>
            </form>
            <#elseif book.borrowed>
            <p style="color: #666; font-style: italic;">Эта книга сейчас у другого сотрудника.</p>
            <#else>
            <p style="color: #f44336; font-weight: bold;">Вы не можете взять больше двух книг одновременно!</p>
        </#if>
    </div>
    <#else>
    <p style="margin-top: 20px;"><a href="/login">Войдите в систему</a>, чтобы взять или вернуть эту книгу.</p>
</#if>
</div>
</div>
</div>
</body>
</html>