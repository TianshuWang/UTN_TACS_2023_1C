FROM python:3.9-slim as builder

WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip3 install --trusted-host pypi.org --trusted-host files.pythonhosted.org -r requirements.txt

COPY . .

CMD ["python3", "-m" , "bot", "run", "--host=0.0.0.0"]