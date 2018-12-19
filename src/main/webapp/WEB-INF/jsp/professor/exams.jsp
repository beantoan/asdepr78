<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:default>
    <jsp:attribute name="title">
      Exams
    </jsp:attribute>

    <jsp:body>
        <div class="row mb-3 d-flex justify-content-between">
            <div class="d-flex justify-content-start h3">
                Your Exams
            </div>
            <div class="d-flex justify-content-end d-lg-none d-xl-none">
                <a class="btn btn-primary btn-raised d-flex align-items-center"
                   href="/professor/exams/create">
                    <i class="material-icons mr-1">note_add</i>Create Exam
                </a>
            </div>
        </div>

        <t:message warning="${warning}"></t:message>

        <div class="row">
            <table class="table table-bordered table-hover">
                <thead class="thead-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Title</th>
                    <th scope="col">Time Range</th>
                    <th scope="col">Status</th>
                    <th scope="col">No. Submitted</th>
                    <th scope="col">No. Reviewed</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="exam" items="${exams}" varStatus="counter">
                    <tr>
                        <th scope="col">${counter.index + 1}</th>
                        <td>${exam.title} (${fn:length(exam.questions)} questions)</td>
                        <td>
                            Open <fmt:formatDate value="${exam.startedAt}"
                                            pattern="HH:mm:ss dd-MM-yyyy"/>
                            <br/>
                            Close <fmt:formatDate value="${exam.finishedAt}"
                                            pattern="HH:mm:ss dd-MM-yyyy"/>
                            <br/>
                            Duration ${exam.duration} minutes
                        </td>
                        <td>${exam.editable ? 'Pending' : (exam.expired ? 'Closed' : 'Open')}</td>
                        <td>${exam.submittedCount}</td>
                        <td>${exam.reviewedCount}</td>
                        <td>
                            <c:if test="${exam.submittedCount gt 0}">
                                <a href="/professor/submissions/${exam.id}"
                                   class="btn btn-primary btn-sm btn-block">
                                    Submissions
                                </a>
                            </c:if>

                            <a href="/professor/exams/view/${exam.id}"
                               class="btn btn-primary btn-sm btn-block">
                                    ${exam.editable ? 'Edit' : 'View'}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </jsp:body>
</t:default>