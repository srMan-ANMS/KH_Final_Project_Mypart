//swal 함수들
/*
  swal은 alert 함수를 예쁘게 표시해주는 라이브러리입니다.
*/
function showFailure(title, text, callback) {
  Swal.fire({
    icon: "error",
    title: title,
    html: text + "이 창은 <b></b> 후에 자동으로 닫힙니다.",
    timer: 2000,
    showConfirmButton: true,
    didOpen: () => {
      Swal.showLoading();
      const b = Swal.getHtmlContainer().querySelector("b");
      timerInterval = setInterval(() => {
        b.textContent = Swal.getTimerLeft();
      }, 100);
    },
    willClose: () => {
      clearInterval(timerInterval);
    },
  }).then(() => {
    if (callback) {
      callback();
    }
  });
}

function showSuccess(title, text, callback) {
  Swal.fire({
    icon: "success",
    title: title,
    text: text,
    html: "이 창은 <b></b> 후에 자동으로 닫힙니다.",
    timer: 2000,
    showConfirmButton: true,
    didOpen: () => {
      Swal.showLoading();
      const b = Swal.getHtmlContainer().querySelector("b");
      timerInterval = setInterval(() => {
        b.textContent = Swal.getTimerLeft();
      }, 100);
    },
    willClose: () => {
      clearInterval(timerInterval);
    },
  }).then(() => {
    if (callback) {
      callback();
    }
  });
}

function showConfirm(title, text, icon, callback) {
  Swal.fire({
    title: "Are you sure?",
    text: "You won't be able to revert this!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Yes, delete it!",
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire("Deleted!", "Your file has been deleted.", "success");
    }
  });
}

function showConfirm(title, text, icon, callback) {
  Swal.fire({
    title: title,
    text: text,
    icon: icon,
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "네!",
    cancelButtonText: "아니요!",
  }).then((result) => {
    if (result.isConfirmed) {
      // Swal.fire("", "Your file has been deleted.", "success");
      callback();
    }
  });
}
