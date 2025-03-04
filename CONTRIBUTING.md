# Contributing to GO Train Tracker Android

Thank you for your interest in contributing to GO Train Tracker Android! This document provides guidelines and instructions for contributing to the project.

## Code of Conduct

By participating in this project, you agree to abide by our Code of Conduct. Please read it before contributing.

## Development Setup

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/MyGOTrainTracker.git
   ```
3. Create a new branch for your feature:
   ```bash
   git checkout -b feat/your-feature-name
   ```
4. Set up your development environment:
   - Install Android Studio
   - Install the required plugins:
     - Kotlin
     - Jetpack Compose
     - KSP
   - Configure your local.properties with your API key
   - Open the project in Android Studio
   - Let Gradle sync complete (this will automatically install git hooks)

Note: Git hooks for conventional commits are automatically installed when you sync the project in Android Studio. No manual setup is required.

## Development Guidelines

### Code Style

- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused
- Use appropriate access modifiers

### Architecture

- Follow Clean Architecture principles
- Keep the layers separate and maintain their boundaries
- Use dependency injection (Koin)
- Implement proper error handling
- Write unit tests for business logic

### UI/UX

- Follow Material Design 3 guidelines
- Ensure accessibility compliance
- Support both light and dark themes
- Handle different screen sizes
- Implement proper error states

### Testing

- Write unit tests for all business logic
- Write UI tests for critical user flows
- Ensure test coverage for new features
- Run all tests before submitting PRs

## Pull Request Process

1. Update your branch with the latest changes from main:
   ```bash
   git fetch origin
   git rebase origin/main
   ```

2. Run the following checks:
   - Run all tests
   - Check code formatting
   - Verify no lint errors
   - Build the project

3. Create a Pull Request:
   - Use the provided PR template
   - Include screenshots for UI changes
   - Link related issues
   - Provide clear description of changes

4. Address review comments and make necessary changes

## Commit Messages

We strictly follow the [Conventional Commits](https://www.conventionalcommits.org/) specification with a simplified format. This is enforced through a pre-commit hook.

### Format
```
type: description

[optional body]

[optional footer]
```

### Types
- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code (white-space, formatting, etc.)
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `perf`: A code change that improves performance
- `test`: Adding missing tests or correcting existing tests
- `build`: Changes that affect the build system or external dependencies
- `ci`: Changes to CI configuration files and scripts
- `chore`: Other changes that don't modify source or test files

### Description
- Use imperative, present tense: "change" not "changed" nor "changes"
- Don't capitalize first letter
- No dot (.) at the end

### Examples
```
feat: add biometric authentication
fix: handle network timeout gracefully
docs: update installation instructions
style: align buttons consistently
refactor: optimize database queries
perf: implement response caching
test: add unit tests for login flow
build: update kotlin to 1.9.0
ci: add sonar analysis
chore: update gradle wrapper
```

### Pre-commit Hook
A pre-commit hook is installed to enforce this convention. If your commit message doesn't follow the convention, the commit will be rejected with an error message explaining the expected format.

To bypass the hook (not recommended):
```bash
git commit -m "your message" --no-verify
```

## Review Process

1. All PRs require at least one review
2. Address all review comments
3. Ensure CI checks pass
4. Merge only after approval

## Documentation

- Update README.md for significant changes
- Add inline documentation for complex code
- Update API documentation if needed
- Document any new dependencies

## Release Process

1. Update version numbers
2. Update changelog
3. Create release branch
4. Run release tests
5. Create release tag
6. Deploy to Play Store

## Getting Help

- Open an issue for bugs
- Use discussions for questions
- Join our community chat
- Contact maintainers

## License

By contributing, you agree that your contributions will be licensed under the project's license with the same restrictions.

Thank you for contributing to GO Train Tracker Android! 