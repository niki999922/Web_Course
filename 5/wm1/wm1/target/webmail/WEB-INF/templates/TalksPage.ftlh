<#import "common.ftl" as c>

<@c.page>
<div class="form-box">
    <div class="header">Send Message</div>
    <div class="body">
        <form method="post" action="/talks">
            <input type="hidden" name="action" value="sendMessage">
            <div class="field">
                <div class="name">
                    <label for="name">Recipient</label>
                </div>
                <div class="value">
                    <input id="name" name="name" value="${name!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="message">Text</label>
                </div>
                <div class="value">
                    <textarea id="message" name="message" value="${message!}"></textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Enter">
            </div>
            <#if error??>
            <div class="error">
                ${error}
            </div>
            </#if>
        </form>
    </div>
</div>
<div class="form-box">
    <div class="header">Show dialog</div>
    <div class="body">
        <form method="post" action="/talks">
            <input type="hidden" name="action" value="showDialog">
            <div class="field">
                <div class="name">
                    <label for="name">Friend name</label>
                </div>
                <div class="value">
                    <input id="name" name="name" value="${name!}"/>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Enter">
            </div>
        </form>
    </div>
</div>
<div class="form-box">
    <div class="header">Show all dialogs</div>
    <div class="body">
        <form method="post" action="/talks">
            <input type="hidden" name="action" value="showAllDialog">
            <div class="button-field">
                <input type="submit" value="Show">
            </div>
        </form>
    </div>
</div>
    <#if messages??>
<div class="datatable">
    <div class="caption">Chat</div>
    <table>
        <thead>
        <tr>
            <th>Text</th>
            <th>From</th>
            <th>To</th>
            <th>Date</th>
        </tr>
        </thead>
        <tbody>
    <#list messages as message>
    <tr>
        <td>
            ${message.textMessage}
        </td>
        <td>
            ${message.sourceUser}
        </td>
        <td>
            ${message.targetUser}
        </td>
        <td>
            ${message.creationTime}
        </td>
    </tr>
    </#list>
    </table>

</div>
    </#if>

</@c.page>
