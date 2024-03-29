# Пользовательские истории

![Прототип пользовательского интерфейса](./mobile_ui_mockup.png)

## 1. Папки

**В качестве** пользователя приложения, \
**Я хочу** иметь возможность группировать/категоризировать свои заметки \
**Для того, чтобы** они отображались упорядоченно и я мог видеть всю структуру своих заметок

1. Сценарий: просмотр структуры заметок \
   **Дано:** в приложении уже есть структура папок и заметок \
   **Когда** я открываю приложение, \
   **тогда** я вижу главное окно, на котором отображается древовидная структура из папок и заметок, \
   **и** отображается последняя ранее открвывавшаяся заметка, \
   **и** ее положение выделено в дереве папок и заметок.

2. Сценарий: создание новой папки \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок.
   **Когда** я выделяю нужную мне папку \
   **и** активирую кнопку создания папки, \
   **Тогда** под выделенной папкой отображается поле редактирования, \
   в котором отображается выделенное сгенерированное название папки (например, "Папка (2)"). \
   **Когда** я в появившемся текстовом поле ввожу название папки и активирую кнопку подтверждения \
   **Тогда** на месте поля редактирования в под выделенной ранее папкой отображается новая дочерняя \
   для нее папка с указанным на предыдущем шаге названием.

3. Сценарий: удаление **пустой** папки \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок. \
   **Когда** я выделяю нужную мне пустую папку \
   **и** активирую кнопку удаления папки, \
   **Тогда** выделенная мной папка не отображается в структуре папок

4. Сценарий: попытка удаление **непустой** папки \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок. \
   **Когда** я выделяю нужную какую-то непустую папку \
   **и** активирую кнопку удаления папки, \
   **Тогда** выводится предупреждаеющее сообщение, что непустые папки удалять нельзя, \
   **и** структура папок не меняется

## 2. Заметки
   
1. Сценарий: создание новой заметки \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок. \
   **Когда** я выделяю нужную мне папку \
   **и** активирую кнопку создания заметки, \
   **Тогда** отображается окно создания/редактирования заметки. \
   **Когда** в окне создания/редактирования заметки я ввожу в полях редактирования тему и тело заметки \
   **и** активирую в этом окне кнопку подтверждения создания заметки, \
   **Тогда** закрывается окно создания/редактирования заметки, \
   **и** тема новой заметки отображается как дочерний узел выделенной ранее папки.

2. Сценарий: удаление заметки \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок. \
   **Когда** я выделяю нужную мне заметку в структуре папок \
   **и** активирую кнопку удаления заметки, \
   **Тогда** отображается окно подтверждения, действительно ли нужно удалить заметку. \
   **Когда** я подтверждаю удаление заметки, \
   **Тогда** ранее выделенная мной заметка не отображается в структуре папок.

3. Сценарий: редактирование заметки \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок. \
   **Когда** я выделяю нужную мне заметку в деререве папок \
   **и** активирую кнопку редактирования заметки, \
   **Тогда** отображается окно создания/редактирования заметки. \
   **Когда** в окне создания/редактирования заметки я меняю в полях редактирования тему и/или тело заметки \
   **и** активирую в этом окне кнопку подтверждения изменения заметки, \
   **Тогда** закрывается окно создания/редактирования заметки, \
   **и** новая тема заметки отображается в дереве папок папки.

4. Сценарий: просмотр \
   **Дано:** открыто основное окно приложения, на котором отображается структура папок. \
   **Когда** я выделяю нужную мне заметку в деререве папок \
   **Тогда** отображается окно просмотра заметки. \   