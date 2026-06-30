<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список книг библиотеки</title>
    <link rel="stylesheet" href="/css/freemarker.css">
</head>
<body>
<div class="container">
    <div style="display: flex; justify-content: space-between; align-items: center; background-color: #f8f9fa; padding: 10px 20px; border-radius: 5px; margin-bottom: 20px;">
        <#if user??>
        <span>Привет, <strong>${user.name}</strong>!</span>
        <div>
            <a href="/profile" style="margin-right: 15px;">Личный кабинет</a>
            <a href="/logout" style="color: red;">Выйти</a>
        </div>
        <#else>
        <span>Вы не авторизованы</span>
        <div>
            <a href="/login" style="margin-right: 15px;">Войти</a>
            <a href="/register">Регистрация</a>
        </div>
    </#if>
</div>

<h1>Список книг</h1>
<table border="1" style="width: 100%; border-collapse: collapse; margin-top: 20px;">
    <thead>
    <tr style="background-color: #eee;">
        <th>Название</th>
        <th>Автор</th>
        <th>Статус</th>
    </tr>
    </thead>
    <tbody>
    <#list books as book>
    <tr>
        <td><a href="/book/${book.id}">${book.title}</a></td>
        <td>${book.author}</td>
        <td>
            <#if book.borrowed>
            <span style="color: red;">Выдана</span>
            <#if book.currentHolder??>
            (сотруднику <a href="/employee/${book.currentHolder.id}">${book.currentHolder.name}</a>)
        </#if>
        <#else>
        <span style="color: green;">Доступна</span>
    </#if>
    </td>
    </tr>
    </#list>
    </tbody>
</table>
</div>
</body>
</html>