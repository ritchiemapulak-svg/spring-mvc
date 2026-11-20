/** Base URL of the student REST endpoints. */
const API_URL = "http://localhost:8080/api/students";
/** Base URL of the authentication endpoints. */
const API_AUTH = "http://localhost:8080/api/auth";

/**
 * Creates a new student record.
 * Submits the student details to the backend and, once the request
 * succeeds, notifies the user and resets the form.
 */
async function createStudent(studentData) {
    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(studentData),
            credentials: "include"
        });

        if (response.ok){
            alert("Student added successfully!");
            resetForm();
            getAllStudents();
        } else {
            alert("Error adding student.");
        }
    } catch (error) {
        console.error("Error creating student:", error);
    }
}

/**
 * Handles submission of the student form.
 * Validates the input, then updates the existing student if an id is
 * present, or creates a new student otherwise.
 */
document.getElementById("studentForm").addEventListener("submit", function(e){
    e.preventDefault();

    if(!validateStudentForm()){
        return;
    }
    const id = document.getElementById("studentId").value;
    const studentData = {
        firstName: document.getElementById("firstName").value.trim(),
        lastName: document.getElementById("lastName").value.trim(),
        email: document.getElementById("email").value.trim()
    };
    if(id) {
        updateStudent(id, studentData);
    } else {
        createStudent(studentData);
    }
});

document.addEventListener("DOMContentLoaded", getAllStudents);

/**
 * Resets the student form to its initial state.
 * Clears all fields and the hidden id, and restores the submit button
 * to its "Add Student" appearance.
 */
function resetForm(){
    document.getElementById("studentId").value = "";
    document.getElementById("studentForm").reset();

    const submitBtn = document.getElementById("submitBtn");
    submitBtn.textContent = "Add Student";
    submitBtn.style.backgroundColor = "#28a745";
}

/**
 * Validates the student form before submission.
 * Checks that the first name, last name and email are provided and
 * well formed, and displays an error message for each invalid field.
 */
function validateStudentForm(){
    let isValid = true;

    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const email = document.getElementById("email");

    const firstNameError = document.getElementById("firstNameError");
    const lastNameError = document.getElementById("lastNameError");
    const emailError = document.getElementById("emailError");

    clearValidationStyles();

    if (firstName.value.trim() === "") {
        firstNameError.textContent = "First name is required.";
        firstName.classList.add("invalid");
        isValid = false;
    } else if (firstName.value.trim().length < 2) {
        firstNameError.textContent = 
        "First name must be at least 2 characters long.";
        firstName.classList.add("invalid");
        isValid = false;
    }

    if (lastName.value.trim() === "") {
        lastNameError.textContent = "Last name is required.";
        lastName.classList.add("invalid");
        isValid = false;
    } else if (lastName.value.trim().length < 2) {
        lastNameError.textContent = 
        "Last name must be at least 2 characters long.";
        lastName.classList.add("invalid");
        isValid = false;
    }

    const emailValue = email.value.trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    
    if (emailValue === "") {
        emailError.textContent = "Email address is required.";
        email.classList.add("invalid");
        isValid = false;
    } else if (!emailRegex.test(emailValue)) {
        emailError.textContent = 
        "Please enter a valid email address (e.g., student@example.com).";
        email.classList.add("invalid");
        isValid = false;
    }
    return isValid;
}

/**
 * Clears the validation feedback from the student form.
 * Removes the error styling and messages from every validated field.
 */
function clearValidationStyles() {
    const fields = ["firstName", "lastName", "email"];
    fields.forEach(fieldId => {
        const element = document.getElementById(fieldId);
        const errorSpan = document.getElementById(`${fieldId}Error`);
        if (element) element.classList.remove("invalid");
        if (errorSpan) errorSpan.textContent = "";
    });
}

/**
 * Retrieves all students from the backend.
 * Populates the student table with one row per student, each with
 * Edit and Delete actions.
 */
async function getAllStudents() {
    try {
        const response = await fetch(API_URL, {credentials: "include"});
        if (!response.ok) throw new Error("Network response was not ok");

        const students = await response.json();
        const tableBody = document.getElementById("studentTableBody");
        tableBody.innerHTML =   "";

        students.forEach(student => {
            const row = `
                <tr>
                    <td>${student.id}</td>
                    <td>${student.firstName}</td>
                    <td>${student.lastName}</td>
                    <td>${student.email}</td>
                    <td>
                        <button class = "btn-edit" onclick =
                        "initializeStudentFormById(${student.id})">Edit
                        </button>
                        <button class = "btn-delete" onclick =
                        "deleteStudent(${student.id})">Delete</button>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    } catch (error){
        console.error("Error fetching students:", error);
        alert(
        "Failed to fetch students. Is your Spring Boot backend running?"
        );
    }
}

/**
 * Prepares the form for editing an existing student.
 * Fetches the student by id, fills the form with their details and
 * switches the submit button to "Update" mode.
 */
async function initializeStudentFormById(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
        credentials: "include"
        });
        if(!response.ok) throw new Error("Could not fetch student details");

        const student = await response.json();

        document.getElementById("studentId").value = student.id;
        document.getElementById("firstName").value = student.firstName;
        document.getElementById("lastName").value = student.lastName;
        document.getElementById("email").value = student.email;

        const submitBtn = document.getElementById("submitBtn");
        submitBtn.textContent = "Update";
        submitBtn.style.backgroundColor = "#007bff"

        clearValidationStyles();
    } catch (error) {
        console.error("Error fetching student details:", error);
    }
}

/**
 * Updates an existing student record.
 * Sends the modified details to the backend and, on success, resets
 * the form and refreshes the student table.
 */
async function updateStudent(id, studentData) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(studentData),
            credentials: "include"
        });

        if (response.ok) {
            alert("Student updated successfully!");
            resetForm();
            getAllStudents();
        } else {
            alert("Error updating student.");
        }
    } catch (error) {
        console.error("Error updating student:", error);
    }
}

/**
 * Deletes a student record.
 * Requests confirmation from the user, removes the student through
 * the backend and refreshes the student table.
 */
async function deleteStudent(id) {
    if (confirm("Are you sure you want to delete this student?")) {
        try {
            const response = await fetch(`${API_URL}/${id}`, {
                method: "DELETE",
                credentials: "include"
            });
            
            if (response.ok) {
                const message = await response.text();
                alert(message);
                getAllStudents();
            } else {
                alert("Error deleting student.");
            }
        } catch (error) {
            console.error("Error deleting student:", error);
        }
    }
}

/**
 * Authenticates a user.
 * Submits the login credentials to the backend and redirects to the
 * main page when the credentials are accepted.
 */
async function loginUser(loginDto) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginDto),
            credentials: "include"
        });
        if (response.ok) {
            alert("Login successful!");
            window.location.href = "index.html";
        } else {
            alert("Invalid Credentials.");
        }
    } catch (error) {
        console.error("Login error:", error);
    }
}

/**
 * Ends the current user session.
 * Logs the user out through the backend, clears the displayed data and
 * form, discards the browser's cached credentials and reloads the page.
 */
async function logoutUser() {
    try {
        await fetch(`${API_AUTH}/logout`, {
            method: "POST",
            credentials: "include"
        });
        document.getElementById("studentTableBody").innerHTML = "";
        resetForm();
        alert("Logged out successfully!");

        try {
            await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Authorization":  "Basic" + 
                    btoa("invalid_user:wrong_password")
                }
            }); 
        } catch (err) {
            console.log("Browser credential cache cleared.");
        }
        window.location.reload();
    } catch (e) {
        console.error("Logout transmission crash:", e);
    }
}


