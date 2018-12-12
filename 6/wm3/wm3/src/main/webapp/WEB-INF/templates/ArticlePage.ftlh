<#import "common.ftl" as c>

<@c.page>
<div class="form-box">
    <div class="header">Add article</div>
    <div class="body">
            <form method="post" action="/article">
            <input type="hidden" name="action" value="addArticle">
            <div class="field">
                <div class="name">
                    <label for="name">Title</label>
                </div>
                <div class="value">
                    <input id="title" name="title"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="message">Text</label>
                </div>
                <div class="value">
                    <textarea id="textArticle" name="textArticle"></textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Create">
            </div>
            <div class="error"></div>
            <div class="info"></div>
        </form>
    </div>
</div>
<script>
    $(function() {
        $(".form-box form").submit(function () {
            var title = $(this).find("input[name='title']").val();
            var textArticle = $(this).find("textarea[name='textArticle']").val();
            var $error = $(this).find(".error").text("");
            var $info = $(this).find(".info").text("");
            $.post("", {action: "addArticle", title: title, textArticle: textArticle}, function (response) {
                if (response["success"]) {
                    $info.text(response["info"])
                } else {
                    $error.text(response["error"]);
                    if (response["error"] == null)  document.location.href = "/";
                }
            });
            return false;
        });
    })
</script>
</@c.page>
