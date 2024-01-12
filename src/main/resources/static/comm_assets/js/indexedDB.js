//indexedDB : 게시글 임시저장
/**
 * indexedDB를 사용한 이유는 임시저장 메커니즘을 만들기 위해서입니다.
 * AWS S3에 저장되게 할 수도 있지만, 악성 유저의 공격을 막기 위해
 * 임시저장은 유저의 로컬에서 담당하도록 했습니다.
 */
//1. PostDatabase 생성하기 : 유저별 고유 스토리지 위해 유저의 아이디 DB명에 사용.
/* 게시글을 임시 저장하기 위해 만들어 놓은 IndexedDB입니다. 이 DB는 전체 게시글 저장에 사용됩니다.*/
var db; //db변수 선언. 나중에 IDBDATABASE객체를 담을 변수
var postDatabaseName = "PostDatabase for " + memberId; //게시글 db이름. 각 사용자마다 할당하기 위해 memberId를 붙여줌
var postStoreName = "postStore"; //게시글 DB 내 테이블(스토어) 이름.

/*처음에는 db이름을 일정하게 하고 poststorename을 바꾸는 식으로 하니 버그가 자꾸 발생해서, 부득이하게 db이름을 유저 아이디 사용해 할당하는 쪽으로 바꿈
      createObjectStore()관련 에러였음.*/

var request = window.indexedDB.open(postDatabaseName, 1); //indexedDB 객체를 postDatabaseName라고 이름지어서 생성. 버전은 : 각각 멤버의 번호
//정확히 말하면 IDBOpenDBRequest객체를 반환함. 이 객체는 indexedDB를 열거나 생성하는데 사용됨.

//indexedDB가 열리지 않았을 때/ 열렸을 때/ 업그레이드가 필요할 때를 각각 이벤트로 처리.
request.onerror = function (event) {
  console.log("Post indexedDB database load error: " + event.target.errorCode);
  showFailure("저런..", "게시글 임시저장소를 불러오는데 실패했어요.");
};

request.onsuccess = function (event) {
  db = request.result; //db 오픈에 성공하면 IDBDATABASE객체를 db에 할당.
  console.log("Post indexedDB database load success");
  console.log("db : " + db);

  //onupgradeneeded 이벤트가 발생하지 않았을 때(즉, DB가 존재한다면) 스토어가 없으면 안되니 생성해줌
  //스토어가 없으면 transaction이 성립하지 않으니까
  if (!db.objectStoreNames.contains(postStoreName)) {
    var objectStore = db.createObjectStore(postStoreName, { keyPath: "id" });
    console.log("Post store created successfully : onsuccess");
  }
};
//onupgradeneeded 이벤트가 발생했을 때 : DB가 존재하지 않아서 새로 생성해야 할 때.
request.onupgradeneeded = function (event) {
  db = event.target.result; //indexedDB에서 사용되는 이벤트 객체의 속성 중 하나, indexedDb 트랜젝션에서 실행된 요청의 결과를 반환
  var objectStore = db.createObjectStore(postStoreName, { keyPath: "id" });
  console.log("Post store created successfully : onupgradeneeded");
};

//3. indexedDB에 임시저장하기 : db.transaction()메서드로 트랜잭션 생성, objectStore.add()(or put)메서드로 데이터 추가
//add는 한번만 더할 수 있고, put은 더한 다음에 똑같은 id로 넣으면 업데이트하므로 put이 적합
function saveTempPost(id) {
  console.log("savetempPost시작,  db체크 : " + db);

  //서머노트 본문과, 현 시점에서 필요한 변수를 저장.
  var snContent = $("#summernote").summernote("code");
  var content = [snContent];
  var transaction = db.transaction(postStoreName, "readwrite"); //스토어와 트랜젝션 수행하고, 모드는 readWrite(읽기쓰기). 트랜젝션 종료시 자동으로 커밋됨.
  var objectStore = transaction.objectStore(postStoreName);
  var request = objectStore.put({ id: id, content: content }); //변수로 받은 id와 현재 서머노트의 content로 스토어에 저장함(key - value로) put을 사용해 중복 시 덮어씌움.

  request.onerror = function (event) {
    showFailure("저장 실패", "게시글 임시저장에 실패했어요.");
  };
  request.onsuccess = function (event) {
    // showSuccess("저장 성공", "게시글이 임시저장되었어요.");
    console.log("게시글 임시저장 성공");
  };
}

//4. indexedDB에 임시저장된 데이터 가져오기 : objectStore.get으로 가져옴.
function loadTempPost(id) {
  console.log("loadtempPost시작,  db체크 : " + db);

  var transaction = db.transaction([postStoreName], "readonly");
  var objectStore = transaction.objectStore(postStoreName);
  var request = objectStore.get(id);

  request.onerror = function (event) {
    showFailure("불러오기 실패", "게시글 임시저장 불러오기에 실패했어요.");
  };
  request.onsuccess = function (event) {
    var content = request.result;
    console.log("임시저장된 content : " + content);
    if (content != null) {
      console.log("게시글 임시저장 불러오기 성공");
      $("#summernote").summernote("code", content.content[0]);
    }
  };
}

//indexedDB에서 임시저장된 데이터 날리기
function clearTempPost() {
  console.log("clearTempPost시작,  db체크 : " + db);
  var transaction = db.transaction([postStoreName], "readwrite");
  var objectStore = transaction.objectStore(postStoreName);
  var clearRequest = objectStore.clear();

  //임시저장했던 postJson을 사용해서 이미지를 날려야 할 필요성이 있음.

  clearRequest.onerror = function (event) {
    console.log("Clear error: " + event.target.errorCode);
  };

  clearRequest.onsuccess = function (event) {
    console.log("Clear success");
  };
}
