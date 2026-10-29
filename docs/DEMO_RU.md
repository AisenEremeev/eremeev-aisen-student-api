# DevOps midterm — Aisen Eremeev, IT2-2314

## Где находится работа

- GitHub: https://github.com/AisenEremeev/eremeev-aisen-student-api
- Jenkins: http://localhost:8080/job/Eremeev_Aisen_Student_API/
- Приложение: http://localhost:8082/
- Студенты: http://localhost:8082/api/students
- Рабочий проект в Ubuntu: `/home/aisen/midterm/eremeev-aisen-student-api`
- Копия исходников и доказательства в Windows: `C:\Projects\DevOps\midterm`

Сервисы работают локально на этом компьютере. Для показа после перезагрузки запусти `START_MIDTERM.cmd` из папки midterm; оставь открывшийся терминал запущенным.

## Что показывать преподавателю

1. Открой GitHub: исходники, `Jenkinsfile`, Dockerfile, Compose, ветки и тег `v1.0-eremeev-aisen`.
2. В Ubuntu перейди в каталог проекта и покажи Linux-команды ниже.
3. Покажи граф Git и сохранённый настоящий конфликт в `evidence/conflict-resolution.txt`.
4. Покажи приложение, контейнеры и ответы CRUD из `evidence/api-crud.txt`. В `container-management.txt` есть остановка/запуск и повторное создание контейнеров без удаления тома.
5. В Jenkins открой историю сборок. В успешной сборке все пять стадий зелёные. В намеренно неуспешной открой Console Output и Test Result: ошибочное ожидание `CONTROLLED_FAILURE`, следующие стадии пропущены.
6. Открой исправленную сборку, затем автоматически запущенную: начало её Console Output содержит `Started by an SCM change`.

## Как запустить Checkout → Build → Test

Открой Jenkins → **Eremeev_Aisen_Student_API** → **Build Now / Собрать сейчас**. Все стадии выполняются последовательно из Jenkinsfile; отдельно нажимать Checkout, Build или Test не нужно. Слева появится новый номер сборки. Открой его → **Console Output**. Успешный результат заканчивается `Finished: SUCCESS`.

Jenkins использует Java 21, Gradle Wrapper 8.14.6 и Docker на локальном учебном агенте. Репозиторий открытый, поэтому для checkout пароль GitHub не нужен. Пароль PostgreSQL остаётся в локальном `.env`, а тесты Jenkins используют H2. Если подключать приватный GitHub или реестр образов, секрет нужно сохранять в Jenkins Credentials и обращаться к нему по `credentialsId`, а не вставлять в Jenkinsfile.

## Команды для Linux-части

В терминале Ubuntu:

```bash
cd /home/aisen/midterm/eremeev-aisen-student-api
pwd
tree -a -I '.git|.gradle|build|.env'
./scripts/Eremeev_Aisen_system_report.sh
chmod 744 scripts/Eremeev_Aisen_system_report.sh
ls -l scripts/Eremeev_Aisen_system_report.sh
cp README.md backups/Eremeev_Aisen_README_backup.md
cmp README.md backups/Eremeev_Aisen_README_backup.md
ls -l backups/
ps -eo pid,user,comm,args | grep '[j]ava'
tail -n 5 logs/Eremeev_Aisen_system_report.log
grep Eremeev logs/Eremeev_Aisen_system_report.log > logs/Eremeev_Aisen_log_analysis.txt
cat logs/Eremeev_Aisen_log_analysis.txt
```

`cmp` без вывода означает одинаковое содержимое. `>` перезаписывает файл; `>>` добавляет в конец. Права `744` — владелец rwx, группа r, остальные r. При Git checkout Linux может восстановить исполняемый файл как 755: перед демонстрацией снова выполни указанный `chmod 744`, поскольку Git хранит только признак исполняемости.

## Git

```bash
git status
git remote -v
git branch -avv
git log --graph --oneline --all
git tag -n
git show 1e158c6 -- README.md
```

В CRUD-ветке два смысловых коммита: реализация PUT с тестами и документация. Конфликт действительно возник из-за изменения одной строки README в main и documentation. При исправлении оба смысла объединены в одной строке, маркеры конфликта удалены, результат добавлен через `git add` и закоммичен.

## Docker и API

```bash
docker compose ps
docker compose logs --tail=30 app
docker compose exec -T app java -version
docker image inspect eremeev-aisen-student-api:v1.0-eremeev-aisen --format 'Size={{.Size}} Created={{.Created}}'
docker inspect eremeev-aisen-student-api-db --format '{{json .Mounts}}'
curl -i http://localhost:8082/
curl -i http://localhost:8082/api/students
```

Для новой CRUD-демонстрации:

```bash
curl -i -X POST http://localhost:8082/api/students -H 'Content-Type: application/json' -d '{"name":"Aisen Eremeev","email":"demo@example.test","groupName":"IT2-2314"}'
```

Возьми `id` из ответа и подставь вместо `ID`:

```bash
curl -i http://localhost:8082/api/students/ID
curl -i -X PUT http://localhost:8082/api/students/ID -H 'Content-Type: application/json' -d '{"name":"Aisen Eremeev","email":"demo@example.test","groupName":"IT2-2314-updated"}'
curl -i -X DELETE http://localhost:8082/api/students/ID
curl -i http://localhost:8082/api/students/ID
```

Ожидаемые ответы: POST 201, GET/PUT 200, DELETE 204, после удаления GET 404. Не удаляй запись с email `persistence@example.test`, если хочешь повторить проверку сохранности исходного примера.

Для перезапуска: `docker compose stop app`, затем `docker compose start app`. Для повторного создания: `docker compose down`, затем `docker compose up -d`. Не добавляй `-v`, если нужно сохранить PostgreSQL. Дождись запуска приложения несколько секунд.

Версия приложения уже изменена с 1.0.0 на 1.0.1 и образ пересобран. До/после сохранены в `evidence/version-before.json` и `version-after.json`.

## Автоматический запуск Jenkins

В Jenkinsfile настроен `pollSCM('* * * * *')`: Jenkins проверяет изменения каждую минуту. После нового коммита в main и `git push` сборка начнётся автоматически, обычно в течение минуты, если Jenkins работает и агент свободен. После изменения только локальных файлов без push сборка не начнётся.

## Короткие ответы для защиты

- **Linux** — семейство ОС на ядре Linux; удобно для серверов, автоматизации и контейнеров. Процесс — запущенная программа; `ps aux`, `top`, `kill PID` (сначала обычный SIGTERM).
- **Shebang** `#!/usr/bin/env bash` выбирает интерпретатор. Аргументы скрипта доступны через `$1`, `$2`, все — `"$@"`.
- **Файлы**: `pwd` — текущий путь; `ls -la` — включая скрытые; `mkdir -p` создаёт цепочку каталогов; `touch` создаёт пустой файл; `cp` копирует, `mv` переносит/переименовывает. Абсолютный путь начинается от корня, относительный — от текущего каталога.
- **Мониторинг**: `df -h` — диски и свободное место, `free -h` — память и swap, `tail -n 5` — последние пять строк, `grep` — поиск текста.
- **chmod** меняет права. 755 = rwx/r-x/r-x; для требуемого скрипта используется 744 = rwx/r--/r--. `chmod +x` добавляет исполняемость.
- **Git** хранит историю версий, GitHub размещает репозитории и средства совместной работы. `git init` создаёт репозиторий; `add` помещает изменения в staging; `commit` фиксирует снимок staging. Рабочие файлы, staging и сохранённая история — три разные области.
- **Ветка** — подвижный указатель на коммит для независимой работы. `git switch -c имя` создаёт ветку. Тег отмечает определённую версию; коммит хранит изменение/снимок, тег ссылается на коммит.
- **fetch** получает удалённые изменения без объединения; **pull** получает и объединяет согласно настройке. **push** отправляет локальные коммиты на сервер. Перед синхронизацией: `git status`, сохранить работу коммитом/stash, `git fetch`, сравнить историю и diff, затем merge/rebase без потери чужих изменений.
- **merge** объединяет истории, иногда создаёт merge-коммит; **rebase** переносит коммиты на другую основу и меняет их идентификаторы. Опубликованную общую историю нельзя бездумно переписывать.
- **Конфликт** возникает, когда Git не может сам совместить изменения. Нужно прочитать обе версии, вручную получить правильный результат, убрать маркеры, `git add` и `git commit`.
- **.gitignore** исключает неотслеживаемые генерируемые/локальные файлы. Секреты нельзя коммитить: история и копии репозитория сохраняют их даже после обычного удаления файла. `.env.example` содержит только шаблон.
- **Docker image** — шаблон файлов и настроек; **container** — запущенный экземпляр образа. `docker build` создаёт образ, `docker stop` останавливает контейнер, `docker rm` удаляет его.
- **Dockerfile**: FROM выбирает основу, WORKDIR задаёт рабочий каталог, COPY переносит файлы, ADD имеет дополнительные возможности (например, распаковку локальных tar). RUN выполняется при сборке, CMD задаёт команду по умолчанию при запуске. ENTRYPOINT задаёт исполняемую команду. EXPOSE описывает порт, но не публикует его; публикация — `-p 8080:8080`.
- **Multi-stage build** собирает JAR в JDK-стадии и переносит только результат в JRE-стадию; инструменты компиляции не нужны в финальном образе.
- **Compose** описывает несколько сервисов в одном YAML. Здесь app и db, отдельная сеть, том и healthcheck. Сеть обеспечивает связь, том хранит данные независимо от жизни контейнеров.
- **Почему db, а не localhost?** localhost внутри app означает сам app-контейнер; имя сервиса db разрешается Docker DNS в контейнер PostgreSQL. Healthcheck `pg_isready` проверяет готовность БД, app ждёт healthy.
- **Секреты БД** передаются через локальный `.env`/окружение или менеджер секретов. Не показывай `docker compose config` и полный `.Config.Env` на записи: там может быть пароль.
- **Jenkins job** — настройка запуска автоматизированной работы. Jenkinsfile — версионируемое описание Pipeline. Controller управляет заданиями, agent выполняет работу. Агенту нужны JDK, доступ к Wrapper и Docker для сборки образов.
- **Стадии**: Checkout получает исходники; Build компилирует и собирает JAR; Test запускает тесты; Docker Build собирает образ; Verify проверяет его наличие. Ошибка команды `sh` завершает стадию ошибкой и пропускает последующие стадии; нельзя маскировать её через `|| true`.
- **Declarative Pipeline** имеет структуру `pipeline { stages { ... } }`; Scripted Pipeline описывается более свободным Groovy-кодом, обычно внутри `node`.
- **Триггер** запускает job при событии/расписании. Webhook — GitHub отправляет событие Jenkins; polling — Jenkins сам проверяет GitHub. Здесь выбран polling, поскольку Jenkins доступен только локально.
- **CI** — частое объединение кода с автоматической сборкой и тестами. **Continuous Delivery** готовит проверенный выпуск, публикация может требовать ручного решения. **Continuous Deployment** автоматически выпускает каждый прошедший проверки результат. В задании реализован CI без автоматического deployment.
- **Диагностика**: при Gradle-ошибке открыть Console Output/Test Result, найти первую причину и повторить команду локально. При ошибке Docker проверить стадию и Dockerfile, права агента и `docker info`, доступ к базовым образам. При ошибке подключения БД — `docker compose ps`, логи app/db, имя db, порт5432, сеть, healthcheck, соответствие пользователя/пароля и существующего тома.

## Доказательства

Папка `evidence` содержит текстовые результаты реальных запусков, историю Git, отчёты тестов, ответы API и консоль Jenkins. Это не скриншоты и не видеозапись. Если преподаватель отдельно требует видео, запиши демонстрацию перечисленных пунктов, не показывая `.env` и пароли.
