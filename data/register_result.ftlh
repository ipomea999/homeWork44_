<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Результат регистрации</title>
        <link rel="stylesheet" href="css/freemarker.css">
    </head>
    <body>
        <div class="container" style="text-align: center; margin-top: 50px;">
            <#if success>
                <h1 style="color: green;">Успешно!</h1>
                <p>${message}</p>
                <p><a href="/login" style="font-size: 18px; font-weight: bold;">Войти в личный кабинет</a></p>
            <#else>
                <h1 style="color: red;">Регистрация не удалась</h1>
                <p>${message}</p>
                <p><a href="/register" style="font-size: 18px; font-weight: bold;">Попробовать снова</a></p>
            </#if>
        </div>
    </body>
</html>