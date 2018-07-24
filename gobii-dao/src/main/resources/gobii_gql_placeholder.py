#!/usr/bin/python
import sys
import random
import getopt

# test data generation script
# by Phil Glaser
def main(argv):

    return_val = 0

    output_file_name = ""
    target_vertex_name = ""
    max_result = 20
    connection_str = ""
    sub_graph_path  = ""
    column_names = ""
    unique = False

    #colon indicates argument required
    opts, args = getopt.getopt(argv, "hc:o:g:t:f:l:uvd", ["connectionString=", "outputFilePath=", "subGraphPath=", "targetVertexName=", "vertexColumnsToFetch=", "verbose", "debug", "unique"])
    for opt, arg in opts:
        if opt in ("-c", "--connectionString"):
            connection_str = arg
        elif opt in ("-o", "--outputFilePath"):
            output_file_name = arg
        elif opt in ("-g", "--subGraphPath"):
            sub_graph_path = arg
        elif opt in ("-t", "--targetVertexName"):
            target_vertex_name = arg
        elif opt in ("-f", "--vertexColumnsToFetch"):
            column_names = arg
        elif opt in ("-l", "--limit"):
            max_result = int(arg)
        elif opt in ("-u", "--unique"):
            unique = True


    print("Initiated gql placeholder test script with arguments ", sys.argv)
    print("output file is: ", output_file_name)
    print("Target vertex is: ", target_vertex_name)
    print("subgraph path is: %s " %sub_graph_path)
    print("max result is: ", max_result)

    file_lines = []
    if str(target_vertex_name) == str("principal_investigator"):
        random_names = [("Ldap", "User1"),
                        ("Shrestha", "User2"),
                        ("Rosyara", "Rosemary"),
                        ("Dreher", "Umesh"),
                        ("Ulat", "Kate"),
                        ("Rutkoski", "Victor"),
                        ("Dreisigacker", "Jessica"),
                        ("Sansaloni", "Susanne"),
                        ("Singh", "Carolina"),
                        ("Singh", "Sukhwinder"),
                        ("Ammar", "Ravi"),
                        ("Sukumaran", "Karim"),
                        ("Mondal", "Siva"),
                        ("Bhavani", "Suchismita"),
                        ("Kumar", "Sridhar"),
                        ("Crossa", "Uttam"),
                        ("Burgueno", "Jose"),
                        ("Braun", "Juan"),
                        ("Lan", "Hans"),
                        ("Reynolds", "Caixia"),
                        ("Molero", "Matthew"),
                        ("Sehgal", "Gemma"),
                        ("Kishii", "Deepmala"),
                        ("Randhawa", "Masahiro"),
                        ("Vikram", "Mandeep"),
                        ("Campos", "Prashant"),
                        ("Banziger", "Jaime"),
                        ("Govindan", "Marianne"),
                        ("Singh", "Velu"),
                        ("He", "Pawan"),
                        ("Crespo", "Xinyao"),
                        ("Basnet", "Leonardo"),
                        ("Baum", "Bhoja"),
                        ("Olsen", "Michael"),
                        ("Puebla", "Michael"),
                        ("Riis", "Luis"),
                        ("Pixley", "Jens"),
                        ("Kropff", "Kevin"),
                        ("Juliana", "Martin"),
                        ("Superuser", "Philomin"),
                        ("Calaminos", "GADM")
                        ]
        length = len(random_names)
        header_name_first = "firstname"
        header_name_last = "lastname"
        # the_file.write("id\t" + header_name_last + "\t" + header_name_first + "\n")
        file_lines.append("id\t" + header_name_last + "\t" + header_name_first)
        idx = 0
        while idx < length:
            last_name = random_names[idx][0]
            first_name = random_names[idx][1]
            # the_file.write("%i\t%s\t%s\n" % (idx, last_name, first_name))
            file_lines.append(str(idx) + "\t" + last_name + "\t" + first_name)
            idx = idx + 1
    else:
        total_lines = 20
        leading_trailing_chars = ""
        id_column = ""
        name_column = ""
        if target_vertex_name == "marker":
            total_lines = random.randint(1, max_result)
            id_column = "id"
            name_column = "name"
        elif target_vertex_name == "dnasample":
            total_lines = random.randint(1, max_result)
            id_column = "id"
            name_column = "name"
        elif (str(target_vertex_name) == str("sampling_date") or
            str(target_vertex_name) == str("genotyping_purpose") or
            str(target_vertex_name) == str("division") or
            str(target_vertex_name) == str("trial_name") or
            str(target_vertex_name) == str("reference_sample") or
            str(target_vertex_name) == str("germplasm_subspecies") or
            str(target_vertex_name) == str("germplasm_species") or
            str(target_vertex_name) == str("genotyping_purpose")):

            if target_vertex_name not in column_names:
                return_val = 3
                sys.stderr.write("KVP type must have fetch column " + target_vertex_name)
            leading_trailing_chars = '"""'
            name_column = target_vertex_name
            id_column = ""
        elif (str(target_vertex_name) == str("analysis") or
              str(target_vertex_name) == str("experiment") or
              str(target_vertex_name) == str("dataset") or
              str(target_vertex_name) == str("linkage_group") or
              str(target_vertex_name) == str("mapset") or
              str(target_vertex_name) == str("vendor") or
              str(target_vertex_name) == str("platform") or
              str(target_vertex_name) == str("project") or
              str(target_vertex_name) == str("protocol") or
              str(target_vertex_name) == str("vendor_protocol")):
                if "name" not in column_names:
                    return_val = 3
                    sys.stderr.write("ENTITY type must have fetch column 'name'")
                id_column = "id"
                name_column = "name"
        else:
                if "term" not in column_names:
                    return_val = 3
                    sys.stderr.write("CVGROUP type must have fetch column 'term'")
                id_column = "id"
                name_column = "term"
                # The left over category should be cv_subset
        if not id_column:
            file_lines.append(name_column)
        else:
            file_lines.append(id_column + "\t" + name_column)

        # the_file.write("id\tname\n")
        idx = 0
        while idx < total_lines:
            idx = idx + 1
            value = leading_trailing_chars + target_vertex_name + " # " + '{:02d}'.format(idx) + leading_trailing_chars
            #the_file.write("%i\t%s\n" % (idx, value))
            if not id_column:
                file_lines.append(value)
            else:
                file_lines.append(str(idx) + "\t" + value)

    with open(output_file_name, 'w') as the_file:
        the_file.write("\n".join(file_lines))
    the_file.close()

    if len(sys.argv) > 0 and str(sys.argv[1]) == "fail":
        print("condition")
        return_val = 1

    sys.exit(return_val)


if __name__ == "__main__":
    main(sys.argv[1:])
