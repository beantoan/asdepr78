<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:default>
    <jsp:attribute name="title">
      Available Exams
    </jsp:attribute>

    <jsp:body>

        <t:message success="${success}" warning="${warning}"></t:message>

        <div class="row justify-content-center">
            <c:forEach var="exam" items="${exams}">
                <div class="card m-3 stu-exam-card">
                    <h5 class="card-header">${exam.title}</h5>

                    <div class="card-body d-flex flex-column justify-content-between">
                        <div class="card-text">${empty exam.desc ? '<i>(No description)</i>' : exam.desc}</div>

                        <div>
                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">face</i>
                                <div>
                                    By ${exam.professor.fullName}
                                </div>
                            </div>

                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">alarm</i>
                                <div>
                                    Open <fmt:formatDate value="${exam.startedAt}" pattern="HH:mm:ss dd-MM-yyyy"/><br/>
                                    Close <fmt:formatDate value="${exam.finishedAt}" pattern="HH:mm:ss dd-MM-yyyy"/>
                                </div>
                            </div>

                            <div class="d-flex align-items-center">
                                <i class="material-icons mr-1">hourglass_empty</i>
                                <div>
                                    Duration ${exam.duration} minutes
                                </div>
                            </div>

                            <a href="/student/exams/start/${exam.id}" class="btn btn-outline-primary btn-block mt-3">
                                Start Exam
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <div class="card m-3 stu-exam-card invisible"></div>
            <div class="card m-3 stu-exam-card invisible"></div>
        </div>
    </jsp:body>
</t:default>