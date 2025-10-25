# Docker Image for Code Execution Environment
# Supports: Java, Python, C++, JavaScript

FROM ubuntu:22.04

# Set non-interactive mode
ENV DEBIAN_FRONTEND=noninteractive

# Install essential tools and compilers
RUN apt-get update && apt-get install -y \
    # Build essentials
    build-essential \
    g++ \
    gcc \
    make \
    # Java Development Kit
    openjdk-17-jdk \
    # Python
    python3 \
    python3-pip \
    # Node.js
    curl \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    # Clean up
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set Java environment
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Create workspace directory
RUN mkdir -p /workspace
WORKDIR /workspace

# Create non-root user for security
RUN useradd -m -u 1000 coderunner && \
    chown -R coderunner:coderunner /workspace

# Switch to non-root user
USER coderunner

# Set default shell
SHELL ["/bin/bash", "-c"]

# Verify installations
RUN java -version && \
    python3 --version && \
    g++ --version && \
    node --version

# Default command
CMD ["/bin/bash"]

