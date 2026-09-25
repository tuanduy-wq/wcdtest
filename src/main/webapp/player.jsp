<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Player Information</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; font-size: 14px; }
        .form-container { border: 1px solid #aaa; padding: 20px; width: 650px; margin-bottom: 25px; }
        .form-container h3 { margin-top: 0; font-weight: normal; }
        .form-row { display: flex; justify-content: space-between; margin-bottom: 12px; }
        .form-col { width: 48%; }
        label { display: block; font-weight: bold; margin-bottom: 5px; }
        input[type="text"], input[type="number"], select { width: 100%; padding: 6px; box-sizing: border-box; }
        .btn-add { background: #eee; border: 1px solid #777; padding: 6px 20px; cursor: pointer; }
        .btn-cancel { background: #ccc; border: 1px solid #777; padding: 6px 20px; cursor: pointer; margin-left: 5px;}
        table { border-collapse: collapse; width: 800px; text-align: left; }
        th, td { border: 1px solid #ccc; padding: 8px; }
        th { background-color: #f2f2f2; }
        .error { color: red; margin-bottom: 15px; font-weight: bold; }
    </style>
</head>
<body>

    <c:if test="${not empty errorMessage}">
        <div class="error">${errorMessage}</div>
    </c:if>

    <div class="form-container">
        <h3>Player Information</h3>
        <form action="player" method="post" id="playerForm">
            <input type="hidden" name="action" id="action" value="add" />
            <input type="hidden" name="id" id="rowId" />
            <input type="hidden" name="playerId" id="playerId" />

            <div class="form-row">
                <div class="form-col">
                    <label>Player name</label>
                    <input type="text" name="playerName" id="playerName" required />
                </div>
                <div class="form-col">
                    <label>Player age</label>
                    <input type="number" name="playerAge" id="playerAge" required />
                </div>
            </div>

            <div class="form-row">
                <div class="form-col">
                    <label>Index name</label>
                    <select name="indexId" id="indexId">
                        <c:forEach items="${indexList}" var="idx">
                            <option value="${idx.indexId}">${idx.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-col">
                    <label>Value</label>
                    <input type="text" name="value" id="value" required />
                </div>
            </div>

            <button type="submit" id="btnSubmit" class="btn-add">Add</button>
            <button type="button" onclick="resetForm()" style="display:none;" id="btnCancel" class="btn-cancel">Cancel</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>Id</th>
                <th>Player name</th>
                <th>Player age</th>
                <th>Index name</th>
                <th>Value</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${playerList}" var="p">
                <tr>
                    <td>${p.id}</td>
                    <td>${p.playerName}</td>
                    <td>${p.playerAge}</td>
                    <td>${p.indexName}</td>
                    <td>${p.value}</td>
                    <td>
                        <a href="#" onclick="fillEditForm('${p.id}', '${p.playerId}', '${p.playerName}', '${p.playerAge}', '${p.indexId}', '${p.value}')" style="text-decoration:none; color:blue;">Edit</a> |
                        <a href="player?action=delete&id=${p.id}" onclick="return confirm('Bạn có chắc chắn muốn xóa?')" style="text-decoration:none; color:blue;">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <script>
        function fillEditForm(id, playerId, name, age, indexId, val) {
            document.getElementById('action').value = 'edit';
            document.getElementById('rowId').value = id;
            document.getElementById('playerId').value = playerId;
            document.getElementById('playerName').value = name;
            document.getElementById('playerAge').value = age;
            document.getElementById('indexId').value = indexId;
            document.getElementById('value').value = val;
            document.getElementById('btnSubmit').innerText = 'Update';
            document.getElementById('btnCancel').style.display = 'inline-block';
        }

        function resetForm() {
            document.getElementById('action').value = 'add';
            document.getElementById('rowId').value = '';
            document.getElementById('playerId').value = '';
            document.getElementById('playerForm').reset();
            document.getElementById('btnSubmit').innerText = 'Add';
            document.getElementById('btnCancel').style.display = 'none';
        }
    </script>
</body>
</html>