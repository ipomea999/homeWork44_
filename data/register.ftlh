<!DOCTYPE html>
<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Регистрация сотрудника</title>
        <link rel="stylesheet" href="css/forms.css">
    </head>
    <body>
        <main>
            <form action="/register" method="post">
                <fieldset>
                    <div class="legend">
                        <p>Регистрация нового сотрудника</p>
                    </div>
                    <div class="form-element">
                        <label for="user-email">Email (Идентификатор)</label>
                        <input type="email" name="email" id="user-email" placeholder="Введите ваш email" required autofocus>
                    </div>
                    <div class="form-element">
                        <label for="user-name">Имя пользователя</label>
                        <input type="text" name="name" id="user-name" placeholder="Введите имя и фамилию" required>
                    </div>
                    <div class="form-element">
                        <label for="user-password">Пароль</label>
                        <input type="password" name="password" id="user-password" placeholder="Введите пароль" required>
                    </div>
                    <div class="hr-line">
                        <span class="details">Остался один шаг</span>
                    </div>
                    <div class="form-element">
                        <button class="register-button" type="submit">Зарегистрироваться</button>
                    </div>
                </fieldset>
            </form>
        </main>
    </body>
</html>