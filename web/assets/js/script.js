document.addEventListener("DOMContentLoaded", function () {
    var cells = document.querySelectorAll(".bingo-cell");
    var questionNumber = document.getElementById("questionNumber");
    var questionText = document.getElementById("questionText");
    var resetGameBtn = document.getElementById("resetGameBtn");
    var currentCellNumber = "";

    function showDefaultQuestion() {
        currentCellNumber = "";
        questionText.classList.add("is-empty");
        questionNumber.textContent = "Ch\u01b0a ch\u1ecdn c\u00e2u h\u1ecfi";
        questionText.innerHTML = "<span class=\"empty-mark\" aria-hidden=\"true\">?</span><span>B\u1ea5m v\u00e0o m\u1ed9t \u00f4 tr\u00ean b\u1ea3ng Bingo \u0111\u1ec3 hi\u1ec3n th\u1ecb c\u00e2u h\u1ecfi.</span>";
    }

    cells.forEach(function (cell) {
        cell.addEventListener("click", function () {
            var cellNumber = cell.dataset.cell;
            var text = cell.dataset.question || "";

            cell.classList.add("selected");
            currentCellNumber = cellNumber;
            questionText.classList.remove("is-empty");
            questionNumber.textContent = "C\u00e2u h\u1ecfi s\u1ed1 " + cellNumber;
            questionText.textContent = text.trim() === ""
                    ? "\u00d4 n\u00e0y ch\u01b0a c\u00f3 c\u00e2u h\u1ecfi. H\u00e3y v\u00e0o Setting \u0111\u1ec3 nh\u1eadp n\u1ed9i dung."
                    : text;
        });

        cell.addEventListener("contextmenu", function (event) {
            event.preventDefault();

            if (!cell.classList.contains("selected")) {
                return;
            }

            cell.classList.remove("selected");

            if (currentCellNumber === cell.dataset.cell) {
                showDefaultQuestion();
            }
        });
    });

    if (resetGameBtn) {
        resetGameBtn.addEventListener("click", function () {
            cells.forEach(function (cell) {
                cell.classList.remove("selected");
            });

            showDefaultQuestion();
        });
    }
});
