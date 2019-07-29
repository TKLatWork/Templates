if [ -d "docker-elk" ]; then
  # Control will enter here if $DIRECTORY exists.
  echo "teardown docker-elk"
  cd docker-elk
  docker-compose down -v
  else
  echo "skip teardown elk"
fi

