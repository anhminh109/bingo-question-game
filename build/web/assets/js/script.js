document.addEventListener("DOMContentLoaded", function () {
    var cells = document.querySelectorAll(".bingo-cell");
    var questionNumber = document.getElementById("questionNumber");
    var questionText = document.getElementById("questionText");
    var resetGameBtn = document.getElementById("resetGameBtn");
    var showAnswerBtn = document.getElementById("showAnswerBtn");
    var answerBox = document.getElementById("answerBox");
    var answerText = document.getElementById("answerText");
    var openGuideBtn = document.getElementById("openGuideBtn");
    var guideModal = document.getElementById("guideModal");
    var closeGuideBtn = document.getElementById("closeGuideBtn");
    var confirmGuideBtn = document.getElementById("confirmGuideBtn");
    var currentCellNumber = "";
    var currentAnswer = "";
    var lastFocusedElement = null;

    function setAnswerOpen(isOpen) {
        if (!answerBox || !showAnswerBtn) {
            return;
        }

        answerBox.classList.toggle("open", isOpen);
        answerBox.setAttribute("aria-hidden", isOpen ? "false" : "true");
        showAnswerBtn.textContent = isOpen ? "Ẩn đáp án" : "Hiển thị đáp án";
    }

    function showDefaultQuestion() {
        currentCellNumber = "";
        currentAnswer = "";
        questionText.classList.add("is-empty");
        questionNumber.textContent = "Chưa chọn câu hỏi";
        questionText.innerHTML = "<span class=\"empty-mark\" aria-hidden=\"true\">?</span><span>Bấm vào một ô trên bảng Bingo để hiển thị câu hỏi.</span>";
        if (answerText) {
            answerText.textContent = "";
        }
        setAnswerOpen(false);
        if (showAnswerBtn) {
            showAnswerBtn.disabled = true;
        }
    }

    function openGuideModal() {
        if (!guideModal) {
            return;
        }

        lastFocusedElement = document.activeElement;
        guideModal.classList.add("is-open");
        guideModal.setAttribute("aria-hidden", "false");
        document.body.classList.add("guide-modal-open");

        if (closeGuideBtn) {
            closeGuideBtn.focus();
        }
    }

    function closeGuideModal() {
        if (!guideModal) {
            return;
        }

        guideModal.classList.remove("is-open");
        guideModal.setAttribute("aria-hidden", "true");
        document.body.classList.remove("guide-modal-open");

        if (lastFocusedElement && typeof lastFocusedElement.focus === "function") {
            lastFocusedElement.focus();
        }
    }

    cells.forEach(function (cell) {
        cell.addEventListener("click", function () {
            var cellNumber = cell.dataset.cell;
            var text = cell.dataset.question || "";

            currentAnswer = cell.dataset.answer || "";
            cell.classList.add("selected");
            currentCellNumber = cellNumber;

            questionText.classList.remove("is-empty");
            questionNumber.textContent = "Câu hỏi số " + cellNumber;
            questionText.textContent = text.trim() === ""
                    ? "Ô này chưa có câu hỏi. Hãy vào Setting để nhập nội dung."
                    : text;

            if (answerText) {
                answerText.textContent = currentAnswer.trim() === ""
                        ? "Ô này chưa có đáp án. Hãy vào Setting để nhập đáp án."
                        : currentAnswer;
            }
            setAnswerOpen(false);
            if (showAnswerBtn) {
                showAnswerBtn.disabled = false;
            }
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

    if (showAnswerBtn) {
        showAnswerBtn.addEventListener("click", function () {
            if (!currentCellNumber || !answerBox) {
                return;
            }

            setAnswerOpen(!answerBox.classList.contains("open"));
        });
    }

    if (resetGameBtn) {
        resetGameBtn.addEventListener("click", function () {
            cells.forEach(function (cell) {
                cell.classList.remove("selected");
            });

            showDefaultQuestion();
        });
    }

    if (openGuideBtn) {
        openGuideBtn.addEventListener("click", openGuideModal);
    }

    if (closeGuideBtn) {
        closeGuideBtn.addEventListener("click", closeGuideModal);
    }

    if (confirmGuideBtn) {
        confirmGuideBtn.addEventListener("click", closeGuideModal);
    }

    if (guideModal) {
        guideModal.addEventListener("click", function (event) {
            if (event.target === guideModal) {
                closeGuideModal();
            }
        });
    }

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && guideModal && guideModal.classList.contains("is-open")) {
            closeGuideModal();
        }
    });
});
